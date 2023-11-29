package com.example.menuapp.domain.usecases

import com.example.menuapp.domain.Repository
import com.example.menuapp.domain.entities.CategoryEntity
import com.example.menuapp.domain.entities.MealEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GetMenuDataUseCase @Inject constructor(
    private val repository: Repository,
) {
    //A pair consisting of the currently selected category and a list
    // of all categories where the current one is marked as "selected".
    private var selectedCategory: Pair<String, List<CategoryEntity>>? = null
    //Flow for emitting lists of categories data where the current one is marked as "selected".
    //New data is emitted when new category is selected from the UI.
    private val parametrizedCategoriesFlow = MutableSharedFlow<List<CategoryEntity>>()
    //Flow for each manually made meal request.
    private val mealsFlow = MutableSharedFlow<List<MealEntity>>()

    fun getCategories(): Flow<List<CategoryEntity>> {
        val repositoryCategoriesFlow = repository.getMealCategories()
            .filter { it.isNotEmpty() }
            .map {
                val categoriesList = it.sortedBy { categoryEntity -> categoryEntity.name }
                selectedCategory
                    ?.let { oldCategory -> setSelectedCategory(oldCategory.first, categoriesList) }
                    ?: run { setSelectedCategory(categoriesList[0].name, categoriesList) }
            }
            .onEach { selectedCategory?.let { setMeals(it.first) } }
        return merge(parametrizedCategoriesFlow, repositoryCategoriesFlow)
    }

    fun getMeals(): Flow<List<MealEntity>> {
        return mealsFlow
    }

    suspend fun selectCategory(category: String) {
        selectedCategory
            ?.let {
                val parametrizedList = setSelectedCategory(category, it.second)
                parametrizedCategoriesFlow.emit(parametrizedList)
                setMeals(category)
            }
    }

    private fun setSelectedCategory(
        category:String,
        categoriesList: List<CategoryEntity>
    ): List<CategoryEntity> {
        val parametrizedList = parametrizeCategoryList(category, categoriesList)
        selectedCategory = Pair(category, parametrizedList)
        return parametrizedList
    }

    private fun parametrizeCategoryList(
        selectedCategory: String,
        oldList: List<CategoryEntity>
    ): List<CategoryEntity> {
        //makes a copy of the current categories with a specific category set as selected
        val newCategoriesList = mutableListOf<CategoryEntity>()
        oldList.forEach {
            if (it.name == selectedCategory) {
                newCategoriesList.add(it.copy(name = it.name, selected = true))
            } else {
                newCategoriesList.add(it.copy(name = it.name, selected = false))
            }
        }
        return newCategoriesList
    }

    private suspend fun setMeals(category: String) {
        mealsFlow.emit(repository.getMealsByCategory(category).sortedBy { it.name })
    }
}