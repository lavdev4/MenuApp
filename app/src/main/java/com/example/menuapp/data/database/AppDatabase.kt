package com.example.menuapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MealDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private var database: AppDatabase? = null
        private const val DB_NAME = "main"
        private val LOCK = Any()

        fun getInstance(context: Context): AppDatabase {
            synchronized(LOCK) {
                database?.let { return it }
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                database = instance
                return instance
            }
        }
    }

    abstract fun mealInfoDao(): MealInfoDao
}