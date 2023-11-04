package com.example.menuapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menuapp.domain.entities.MealEntity
import com.example.menuapp.domain.usecases.GetMealCategoriesUseCase
import com.example.menuapp.domain.usecases.GetMealsByCategoryUseCase
import com.example.menuapp.domain.usecases.LoadDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuViewModel @Inject constructor(
    private val loadDataUseCase: LoadDataUseCase,
    private val deleteDataUseCase: LoadDataUseCase,
    private val getMealCategoriesUseCase: GetMealCategoriesUseCase,
    private val getMealsByCategoryUseCase: GetMealsByCategoryUseCase
) : ViewModel() {

    fun loadData() {
        viewModelScope.launch { loadDataUseCase }
    }

    fun deleteData() {
        viewModelScope.launch { deleteDataUseCase }
    }

    fun getCategories(): Flow<List<String>> {
        return getMealCategoriesUseCase()
    }

    fun getMeals(category: String): Flow<List<MealEntity>> {
        return getMealsByCategoryUseCase(category)
    }
}