package com.example.menuapp.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MealDto(
    @SerializedName("idMeal")
    @Expose
    val id: Int?,
    @SerializedName("strMeal")
    @Expose
    val name: String?,
)
