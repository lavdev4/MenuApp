package com.example.menuapp.presentation

import android.app.Application
import androidx.work.Configuration
import com.example.menuapp.data.workers.AppWorkerFactory
import com.example.menuapp.di.ApplicationComponent
import com.example.menuapp.di.DaggerApplicationComponent
import javax.inject.Inject

class MenuApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var appWorkerFactory: AppWorkerFactory
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        applicationComponent = DaggerApplicationComponent.builder()
            .context(applicationContext)
            .apiBaseUrl(BASE_URL)
            .build()
        applicationComponent.inject(this)
        super.onCreate()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(appWorkerFactory)
            .build()
    }

    companion object {
        private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
    }
}