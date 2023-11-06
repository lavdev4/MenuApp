package com.example.menuapp.domain

import com.example.menuapp.domain.entities.CategoryEntity
import com.example.menuapp.domain.entities.MealEntity
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getMealCategories(): Flow<List<CategoryEntity>>

    suspend fun getMealsByCategory(category: String): List<MealEntity>

    suspend fun loadData()

    suspend fun deleteData()
}