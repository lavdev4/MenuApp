package com.example.menuapp.data.workers

import android.content.Context
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import androidx.work.rxjava3.RxWorker
import androidx.work.workDataOf
import com.example.menuapp.data.database.MealInfoDao
import com.example.menuapp.data.mappers.MealsMapper
import com.example.menuapp.data.network.ApiService
import com.example.menuapp.data.network.model.HttpErrorDto
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.core.SingleOperator
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.toObservable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.util.Optional
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@Suppress("SameParameterValue")
class LoadDataWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val dao: MealInfoDao,
    private val apiService: ApiService,
    private val mapper: MealsMapper,
) : RxWorker(context, workerParams) {

    companion object {
        var WORKER_TAG = LoadDataWorker::class.toString()
        const val ERROR_DATA_TAG = "OnLoadError"
        const val PROGRESS_DATA_TAG = "LoaderProgress"
        const val PROGRESS_VALUE_PREPARE_FINISHED = 0
        private const val NETWORK_REQUEST_DELAY = 70L
    }

    override fun getBackgroundScheduler(): Scheduler {
        return Schedulers.io()
    }

    override fun createWork(): Single<Result> {
        return apiService.getCategories()
            //extract categories from container and emit
            .map { response ->
                response.categoriesContainers ?: throw IOException("Empty network response")
            }
            .flattenAsObservable { categories ->
                categories.mapNotNull { categoryDto -> categoryDto.name }.sorted()
            }
            //get meal names containers for each category and emit grouped
            .concatMapSingle { category ->
                apiService.getMealsByCategory(category)
            }
            //extract meal names from containers and emit grouped
            .mapOptional { response -> Optional.ofNullable(response.mealContainers) }
            .concatMap { names -> names.mapNotNull { mealDto -> mealDto.name }.toObservable() }
            //collect all and then proceed
            .toList()
            .doOnSuccess {
                //remove all data from DB before inserting
                dao.deleteAllData()
                //notify UI of finishing preparations
                setCompletableProgress(progressData(PROGRESS_VALUE_PREPARE_FINISHED))
            }
            //emit meal names
            .flattenAsObservable { it }
            //set delay between emits to avoid HTTP 429 error further
            .compose(synchronize(NETWORK_REQUEST_DELAY, TimeUnit.MILLISECONDS))
            //get info about each meal and emit
            .flatMapSingle { mealName ->
                apiService.getMealInfo(mealName)
                    .retryWhen(httpErrorRetryPolicy(3, 10000))
            }
            //extract meal info from containers
            .mapOptional { Optional.ofNullable(it.mealInfoContainers) }
            //map to DB models and insert in DB
            .map { mealsInfo -> mealsInfo.mapNotNull { mapper.mapMealInfoDtoToMealDbModel(it) } }
            .doOnNext { dao.insert(it) }
            //collect all and then emit ListenableWorker.Result
            .count()
            .lift(toWorkerResult())
    }

    private fun <T : Any> synchronize(
        interval: Long,
        timeUnit: TimeUnit
    ): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.zipWith(
                Observable.interval(interval, timeUnit)
            ) { sourceEmit: T, _: Long -> sourceEmit }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Result> toWorkerResult(): SingleOperator<T, Any> {
        return SingleOperator { observer ->
            object : SingleObserver<Any> {
                override fun onSubscribe(d: Disposable) {
                    observer.onSubscribe(d)
                }

                override fun onError(e: Throwable) {
                    observer.onSuccess(Result.failure(getErrorData(e)) as T)
                }

                override fun onSuccess(t: Any) {
                    observer.onSuccess(Result.success() as T)
                }
            }
        }
    }

    private fun getErrorData(throwable: Throwable): Data {
        return when (throwable) {
            is HttpException -> {
                val response = throwable.response()
                val errorBody = response?.errorBody()?.string()
                if (response != null && errorBody?.isNotEmpty() == true) {
                    val error = Gson().fromJson(errorBody, HttpErrorDto::class.java)
                    errorData(
                        "Network error: ${error.message}Code: ${error.code}."
                    )
                } else if (response != null) {
                    errorData("Network error. Code: ${response.code()}.")
                } else {
                    errorData("Network error: $throwable")
                }
            }

            is IOException -> errorData("Network error: $throwable")
            else -> errorData("Unknown network error.")
        }
    }

    private fun progressData(value: Int) = workDataOf(PROGRESS_DATA_TAG to value)

    private fun errorData(value: String) = workDataOf(ERROR_DATA_TAG to value)

    private fun httpErrorRetryPolicy(retries: Int, delayMillis: Long) = { errors: Flowable<Throwable> ->
        val counter = AtomicInteger(0)
        errors
            .flatMap { throwable ->
                if (counter.getAndIncrement() == retries) return@flatMap Flowable.error(throwable)
                if (throwable is HttpException && throwable.code() == 429) {
                    return@flatMap Flowable.timer(delayMillis, TimeUnit.MILLISECONDS)
                } else {
                    return@flatMap Flowable.error(throwable)
                }
            }
    }

    class Factory @Inject constructor(
        private val dao: MealInfoDao,
        private val apiService: ApiService,
        private val mapper: MealsMapper,
    ) : ChildWorkerFactory {

        override fun create(
            context: Context,
            workerParameters: WorkerParameters,
        ): ListenableWorker {
            return LoadDataWorker(
                context,
                workerParameters,
                dao,
                apiService,
                mapper
            )
        }
    }
}