package com.example.menuapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menuapp.domain.usecases.GetMenuDataUseCase
import com.example.menuapp.domain.usecases.UpdateMenuDataUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuViewModel @Inject constructor(
    updateDataUseCase: UpdateMenuDataUseCase,
    private val getMenuDataUseCase: GetMenuDataUseCase
) : ViewModel() {

    val dataLoadObservable = updateDataUseCase.updateData()
    val categoriesFlow = getMenuDataUseCase.getCategories()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
        .filterNotNull()
    val mealsFlow = getMenuDataUseCase.getMeals()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
        .filterNotNull()

    fun setSelectedCategory(category: String) {
        viewModelScope.launch { getMenuDataUseCase.selectCategory(category) }
    }
}