package com.example.menuapp.di.modules

import android.content.Context
import com.example.menuapp.data.RepositoryImpl
import com.example.menuapp.data.database.AppDatabase
import com.example.menuapp.data.database.MealInfoDao
import com.example.menuapp.data.network.ApiFactory
import com.example.menuapp.data.network.ApiService
import com.example.menuapp.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class DataModule{

    @Binds
    abstract fun bindRepository(impl: RepositoryImpl): Repository

    companion object {
        @Provides
        fun provideDao(context: Context): MealInfoDao {
            return AppDatabase.getInstance(context).mealInfoDao()
        }

        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}