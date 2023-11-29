package com.example.menuapp.data.network.model

import com.google.gson.annotations.SerializedName
import kotlin.reflect.full.declaredMemberProperties

data class MealInfoDto(
    @SerializedName("idMeal")
    val id: Int?,
    @SerializedName("strMeal")
    val name: String?,
    @SerializedName("strCategory")
    val category: String?,
    @SerializedName("strMealThumb")
    val img: String?,
    @SerializedName("strInstructions")
    val instruction: String?) {

    fun hasNonNullAttributes(): Boolean {
        return this::class.declaredMemberProperties.all { it.getter.call(this) != null }
    }
}



