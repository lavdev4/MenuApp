package com.example.menuapp.domain.entities

sealed class LoadResultEntity {
    data object Enqueued : LoadResultEntity()
    data object LoadStarted : LoadResultEntity()
    data object Loaded : LoadResultEntity()
    data object Finished : LoadResultEntity()
    data class Failed(val message: String) : LoadResultEntity()
}