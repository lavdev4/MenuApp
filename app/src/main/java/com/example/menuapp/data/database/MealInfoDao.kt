package com.example.menuapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(meals: List<MealDbModel>)

    @Query("DELETE FROM meal_list")
    fun deleteAllData()

    @Query("SELECT DISTINCT category FROM meal_list")
    fun getMealCategories(): Flow<List<String>>

    @Query("SELECT * FROM meal_list WHERE category = :category")
    suspend fun getMealsByCategory(category: String): List<MealDbModel>
}