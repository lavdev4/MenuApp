package com.example.menuapp.data.network.model

import com.google.gson.annotations.SerializedName

data class MealListDto(
    @SerializedName("meals")
    val mealContainers: List<MealDto>?
)
