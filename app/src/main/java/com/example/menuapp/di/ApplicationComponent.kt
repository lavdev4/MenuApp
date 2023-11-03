package com.example.menuapp.di

import android.content.Context
import com.example.menuapp.di.annotations.ApplicationScope
import com.example.menuapp.presentation.MenuApplication
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [])
interface ApplicationComponent {

    fun inject(app: MenuApplication)

    fun activitySubcomponent(): MainActivitySubcomponent.MainActivitySubcomponentBuilder

    @Component.Builder
    interface ApplicationComponentBuilder {

        @BindsInstance
        fun context(context: Context) : ApplicationComponentBuilder

        fun build(): ApplicationComponent
    }
}