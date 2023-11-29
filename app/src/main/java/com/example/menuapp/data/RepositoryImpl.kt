package com.example.menuapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.menuapp.data.database.MealInfoDao
import com.example.menuapp.data.mappers.MealsMapper
import com.example.menuapp.data.workers.LoadDataWorker
import com.example.menuapp.di.annotations.ApplicationScope
import com.example.menuapp.domain.Repository
import com.example.menuapp.domain.entities.CategoryEntity
import com.example.menuapp.domain.entities.LoadResultEntity
import com.example.menuapp.domain.entities.MealEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ApplicationScope
class RepositoryImpl @Inject constructor(
    private val dao: MealInfoDao,
    private val mapper: MealsMapper,
    private val workManager: WorkManager,
) : Repository {

    override fun getMealCategories(): Flow<List<CategoryEntity>> {
        return dao.getMealCategories().map { dbModels ->
            dbModels.map { mapper.mapCategoryStrToCategoryEntity(it) }
        }
    }

    override suspend fun getMealsByCategory(category: String): List<MealEntity> {
        return dao.getMealsByCategory(category).map { dbModels ->
            mapper.mapMealDbModelToMealEntity(dbModels)
        }
    }

    override fun updateData(): LiveData<LoadResultEntity> {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val worker = OneTimeWorkRequest.Builder(LoadDataWorker::class.java)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork(
            LoadDataWorker.WORKER_TAG,
            ExistingWorkPolicy.REPLACE,
            worker
        )
        return workManager.getWorkInfoByIdLiveData(worker.id)
            .map { mapper.mapWorkInfoToLoadResultEntity(it) }
    }
}