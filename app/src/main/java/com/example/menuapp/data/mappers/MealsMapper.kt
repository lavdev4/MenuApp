package com.example.menuapp.data.mappers

import com.example.menuapp.data.database.MealDbModel
import com.example.menuapp.data.network.model.MealInfoDto
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
        mealInfoDto.id?.let {
            return MealDbModel(
                id = mealInfoDto.id,
                name = mealInfoDto.name,
                category = mealInfoDto.category,
                img = mealInfoDto.img,
                instruction = mealInfoDto.instruction
            )
        }
        return null
    }
}