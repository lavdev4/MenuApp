package com.example.menuapp.data.database

import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface MealInfoDao {
    @Query("SELECT category FROM meal_list")
    fun getMealCategories(): Flow<List<String>>

    @Query("SELECT * FROM meal_list WHERE category = :category")
    fun getMealsByCategory(category: String): Flow<List<MealDbModel>>
}