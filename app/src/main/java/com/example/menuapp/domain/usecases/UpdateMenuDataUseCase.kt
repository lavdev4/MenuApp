package com.example.menuapp.domain.usecases

import androidx.lifecycle.LiveData
import com.example.menuapp.domain.Repository
import com.example.menuapp.domain.entities.LoadResultEntity
import javax.inject.Inject

class UpdateMenuDataUseCase @Inject constructor(
    private val repository: Repository
) {

    fun updateData(): LiveData<LoadResultEntity> {
        return repository.updateData()
    }
}