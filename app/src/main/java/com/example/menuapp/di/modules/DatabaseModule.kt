package com.example.menuapp.di.modules

import android.content.Context
import androidx.room.Room
import com.example.menuapp.data.database.AppDatabase
import com.example.menuapp.data.database.MealInfoDao
import com.example.menuapp.di.annotations.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
interface DatabaseModule {

    companion object {
        @ApplicationScope
        @Provides
        fun provideMainDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME)
                .build()
        }

        @ApplicationScope
        @Provides
        fun provideMealInfoDao(database: AppDatabase): MealInfoDao {
            return database.mealInfoDao()
        }
    }
}