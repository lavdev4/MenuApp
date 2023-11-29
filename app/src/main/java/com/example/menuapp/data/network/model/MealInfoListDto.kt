package com.example.menuapp.data.network.model

import com.google.gson.annotations.SerializedName

data class MealInfoListDto(
    @SerializedName("meals")
    val mealInfoContainers: List<MealInfoDto>?
)
