package com.example.menuapp.di.modules

import android.content.Context
import androidx.work.WorkManager
import com.example.menuapp.data.RepositoryImpl
import com.example.menuapp.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class DataModule {

    @Binds
    abstract fun bindRepository(impl: RepositoryImpl): Repository

    companion object {
        @Provides
        fun provideWorkManager(context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }
    }
}