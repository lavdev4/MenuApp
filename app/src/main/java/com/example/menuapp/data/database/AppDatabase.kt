package com.example.menuapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MealDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "main_database"
    }

    abstract fun mealInfoDao(): MealInfoDao
}