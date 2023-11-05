package com.example.menuapp.di

import com.example.menuapp.di.annotations.MainActivityScope
import com.example.menuapp.presentation.fragment.MenuFragment
import dagger.Subcomponent

@MainActivityScope
@Subcomponent
interface MainActivitySubcomponent {

    fun inject(impl: MenuFragment)

    @Subcomponent.Builder
    interface MainActivitySubcomponentBuilder {

        fun build(): MainActivitySubcomponent
    }
}