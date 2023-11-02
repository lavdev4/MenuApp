package com.example.menuapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_list")
data class MealDbModel(
    @PrimaryKey
    val id: Int,
    val name: String?,
    val category: String?,
    val img: String?,
    val instruction: String?,
)
