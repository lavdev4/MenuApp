package com.example.menuapp.domain.usecases

import com.example.menuapp.domain.Repository
import javax.inject.Inject

class GetMealsByCategoryUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(category: String) = repository.getMealsByCategory(category)
}