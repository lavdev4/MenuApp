package com.example.menuapp.di

import com.example.menuapp.presentation.MainActivity
import com.example.menuapp.di.annotations.MainActivityScope
import dagger.Subcomponent

@MainActivityScope
@Subcomponent
interface MainActivitySubcomponent {

    fun inject(impl: MainActivity)

    @Subcomponent.Builder
    interface MainActivitySubcomponentBuilder {

        fun build(): MainActivitySubcomponent
    }
}