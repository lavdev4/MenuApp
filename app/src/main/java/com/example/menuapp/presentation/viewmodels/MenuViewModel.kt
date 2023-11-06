package com.example.menuapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menuapp.domain.entities.CategoryEntity
import com.example.menuapp.domain.entities.MealEntity
import com.example.menuapp.domain.usecases.DeleteDataUseCase
import com.example.menuapp.domain.usecases.GetMealCategoriesUseCase
import com.example.menuapp.domain.usecases.GetMealsByCategoryUseCase
import com.example.menuapp.domain.usecases.LoadDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuViewModel @Inject constructor(
    private val loadDataUseCase: LoadDataUseCase,
    private val deleteDataUseCase: DeleteDataUseCase,
    private val getMealCategoriesUseCase: GetMealCategoriesUseCase,
    private val getMealsByCategoryUseCase: GetMealsByCategoryUseCase,
) : ViewModel() {

    private var selectedCategory: String? = null
    private var currentCategories: List<CategoryEntity>? = null
    private val categoriesFlow = MutableSharedFlow<List<CategoryEntity>>()
    private val mealsFlow = MutableSharedFlow<List<MealEntity>>()

    fun loadData() {
        viewModelScope.launch { loadDataUseCase() }
    }

    fun deleteData() {
        viewModelScope.launch { deleteDataUseCase() }
    }

    fun setSelectedCategory(category: String) {
        viewModelScope.launch {
            setNewCategory(category)
            setMeals()
        }
    }

    private suspend fun setNewCategory(category: String) {
        selectedCategory = category
        val newCategories = mutableListOf<CategoryEntity>()
        currentCategories?.forEach {
            if (it.name == category) {
                newCategories.add(it.copy(name = it.name, selected = true))
            } else {
                newCategories.add(it.copy(name = it.name, selected = false))
            }
        }
        currentCategories = newCategories
        currentCategories?.let { categoriesFlow.emit(it) }
    }

    fun getCategories(): Flow<List<CategoryEntity>> {
        return getMealCategoriesUseCase()
            .onEach {
                if (selectedCategory == null && it.isNotEmpty()) {
                    selectedCategory = it.first().name
                }
                setMeals()
            }
            .map { currentEmit ->
                currentEmit.map {
                    if (it.name == selectedCategory) it.copy(selected = true) else it
                }
            }
            .onEach { currentCategories = it }
            .mergeWith(categoriesFlow)
    }

    private suspend fun setMeals() {
        selectedCategory?.let { mealsFlow.emit(getMealsByCategoryUseCase(it)) }
    }

    fun getMeals(): SharedFlow<List<MealEntity>> {
        return mealsFlow.asSharedFlow()
    }

    private fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> {
        return merge(this, another)
    }
}