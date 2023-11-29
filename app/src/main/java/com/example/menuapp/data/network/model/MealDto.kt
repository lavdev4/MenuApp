package com.example.menuapp.data.network.model

import com.google.gson.annotations.SerializedName

data class MealDto(
    @SerializedName("idMeal")
    val id: Int?,
    @SerializedName("strMeal")
    val name: String?,
)
