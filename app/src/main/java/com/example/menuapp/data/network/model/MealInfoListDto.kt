package com.example.menuapp.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MealInfoListDto(
    @SerializedName("meals")
    @Expose
    val mealInfoContainers: List<MealInfoDto>?
)
