package com.example.menuapp.presentation

import android.app.Application
import com.example.menuapp.di.ApplicationComponent
import com.example.menuapp.di.DaggerApplicationComponent

class MenuApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        applicationComponent = DaggerApplicationComponent.builder()
            .context(applicationContext)
            .build()
        super.onCreate()
    }
}