package com.example.menuapp.domain.usecases

import com.example.menuapp.domain.Repository
import javax.inject.Inject

class LoadDataUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke() = repository.loadData()
}