package com.example.menuapp.di.modules

import androidx.lifecycle.ViewModel
import com.example.menuapp.di.annotations.ViewModelKey
import com.example.menuapp.presentation.viewmodels.MenuViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @IntoMap
    @ViewModelKey(MenuViewModel::class)
    @Binds
    abstract fun bindMenuViewModel(impl: MenuViewModel): ViewModel
}