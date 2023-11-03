package com.example.menuapp.domain

import com.example.menuapp.domain.entities.MealEntity
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getMealCategories(): Flow<List<String>>

    fun getMealsByCategory(category: String): Flow<List<MealEntity>>

    fun loadData()
}