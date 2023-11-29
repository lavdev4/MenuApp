package com.example.menuapp.domain.entities

data class MealEntity(
    val id: Int,
    val name: String,
    val category: String,
    val img: String,
    val instruction: String,
)