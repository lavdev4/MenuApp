package com.example.menuapp.di

import android.content.Context
import com.example.menuapp.di.annotations.ApplicationScope
import com.example.menuapp.di.modules.DataModule
import com.example.menuapp.di.modules.DatabaseModule
import com.example.menuapp.di.modules.NetworkModule
import com.example.menuapp.di.modules.ViewModelModule
import com.example.menuapp.di.modules.WorkerModule
import com.example.menuapp.presentation.MenuApplication
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [
        DataModule::class,
        ViewModelModule::class,
        WorkerModule::class,
        NetworkModule::class,
        DatabaseModule::class
    ]
)
interface ApplicationComponent {

    fun inject(app: MenuApplication)

    fun activitySubcomponent(): MainActivitySubcomponent.MainActivitySubcomponentBuilder

    @Component.Builder
    interface ApplicationComponentBuilder {

        @BindsInstance
        fun context(context: Context) : ApplicationComponentBuilder

        @BindsInstance
        fun apiBaseUrl(url: String): ApplicationComponentBuilder

        fun build(): ApplicationComponent
    }
}