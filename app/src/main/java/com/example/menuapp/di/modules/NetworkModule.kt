package com.example.menuapp.di.modules

import com.example.menuapp.data.network.ApiService
import com.example.menuapp.di.annotations.ApplicationScope
import dagger.Module
import dagger.Provides
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
interface NetworkModule {

    companion object {
        @ApplicationScope
        @Provides
        fun provideConverterFactory(): Converter.Factory {
            return GsonConverterFactory.create()
        }

        @ApplicationScope
        @Provides
        fun provideCallAdapterFactory(): CallAdapter.Factory {
            return RxJava3CallAdapterFactory.create()
        }

        @ApplicationScope
        @Provides
        fun provideRetrofit(
            baseUrl: String,
            converterFactory: Converter.Factory,
            callAdapterFactory: CallAdapter.Factory,
        ): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .baseUrl(baseUrl)
                .build()
        }

        @ApplicationScope
        @Provides
        fun provideApiService(retrofit: Retrofit): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }
}