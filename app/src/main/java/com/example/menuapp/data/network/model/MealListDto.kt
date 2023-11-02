package com.example.menuapp.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MealListDto(
    @SerializedName("meals")
    @Expose
    val mealContainers: List<MealDto>?
)
