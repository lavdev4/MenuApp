package com.example.menuapp.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MealInfoDto(
    @SerializedName("idMeal")
    @Expose
    val id: Int?,
    @SerializedName("strMeal")
    @Expose
    val name: String?,
    @SerializedName("strCategory")
    @Expose
    val category: String?,
    @SerializedName("strMealThumb")
    @Expose
    val img: String?,
    @SerializedName("strInstructions")
    @Expose
    val instruction: String?
)
