package com.example.menuapp.data.mappers

import android.util.Log
import androidx.work.WorkInfo
import com.example.menuapp.data.database.MealDbModel
import com.example.menuapp.data.network.model.MealInfoDto
import com.example.menuapp.data.workers.LoadDataWorker
import com.example.menuapp.domain.entities.CategoryEntity
import com.example.menuapp.domain.entities.LoadResultEntity
import com.example.menuapp.domain.entities.MealEntity
import javax.inject.Inject

class MealsMapper @Inject constructor() {

    fun mapMealDbModelToMealEntity(mealDbModel: MealDbModel): MealEntity {
        return MealEntity(
            id = mealDbModel.id,
            name = mealDbModel.name,
            category = mealDbModel.category,
            img = mealDbModel.img,
            instruction = mealDbModel.instruction
        )
    }

    fun mapMealInfoDtoToMealDbModel(mealInfoDto: MealInfoDto): MealDbModel? {
        return mealInfoDto.takeIf {
            it.hasNonNullAttributes()
        }?.let {
            MealDbModel(
                id = mealInfoDto.id!!,
                name = mealInfoDto.name!!,
                category = mealInfoDto.category!!,
                img = mealInfoDto.img!!,
                instruction = mealInfoDto.instruction!!
            )
        }
    }

    fun mapCategoryStrToCategoryEntity(categoryStr: String): CategoryEntity {
        return CategoryEntity(name = categoryStr, selected = false)
    }

    fun mapWorkInfoToLoadResultEntity(workInfo: WorkInfo): LoadResultEntity {
        val state = workInfo.state
        val progress = workInfo.progress.keyValueMap[LoadDataWorker.PROGRESS_DATA_TAG]
        val errorData = workInfo.outputData.getString(LoadDataWorker.ERROR_DATA_TAG)
        Log.d("Load state: ", "state: $state, progress: $progress, error: $errorData")
        return when (state) {
            WorkInfo.State.ENQUEUED -> { LoadResultEntity.Enqueued }
            WorkInfo.State.RUNNING -> {
                if (progress != null && progress == LoadDataWorker.PROGRESS_VALUE_PREPARE_FINISHED) {
                    LoadResultEntity.Loaded
                } else {
                    LoadResultEntity.LoadStarted
                }
            }
            WorkInfo.State.SUCCEEDED -> { LoadResultEntity.Finished }
            WorkInfo.State.FAILED -> {
                LoadResultEntity.Failed(errorData ?: "Unknown network exception.")
            }
            else -> { LoadResultEntity.Finished }
        }
    }
}