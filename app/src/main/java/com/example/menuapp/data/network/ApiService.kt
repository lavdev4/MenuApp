package com.example.menuapp.data.network

import com.example.menuapp.data.network.model.CategoryListDto
import com.example.menuapp.data.network.model.MealInfoListDto
import com.example.menuapp.data.network.model.MealListDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("categories.php")
    fun getCategories(): Single<CategoryListDto>

    @GET("filter.php")
    fun getMealsByCategory(
        @Query(QUERY_PARAM_CATEGORY) category: String
    ): Single<MealListDto>

    @GET("search.php")
    fun getMealInfo(
        @Query(QUERY_PARAM_MEAL_NAME) mealName: String
    ): Single<MealInfoListDto>

    companion object {
        private const val QUERY_PARAM_CATEGORY = "c"
        private const val QUERY_PARAM_MEAL_NAME = "s"
    }
}