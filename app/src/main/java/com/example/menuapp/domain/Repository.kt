package com.example.menuapp.domain

import androidx.lifecycle.LiveData
import com.example.menuapp.domain.entities.CategoryEntity
import com.example.menuapp.domain.entities.LoadResultEntity
import com.example.menuapp.domain.entities.MealEntity
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getMealCategories(): Flow<List<CategoryEntity>>

    suspend fun getMealsByCategory(category: String): List<MealEntity>

    fun updateData(): LiveData<LoadResultEntity>
}