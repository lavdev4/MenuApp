package com.example.menuapp.data.network

import com.example.menuapp.data.network.model.CategoryListDto
import com.example.menuapp.data.network.model.MealInfoListDto
import com.example.menuapp.data.network.model.MealListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("categories.php")
    suspend fun getCategories(): CategoryListDto

    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query(QUERY_PARAM_CATEGORY) category: String
    ): MealListDto

    @GET("search.php")
    suspend fun getMealInfo(
        @Query(QUERY_PARAM_MEAL_NAME) mealName: String
    ): MealInfoListDto

    companion object {
        private const val QUERY_PARAM_CATEGORY = "c"
        private const val QUERY_PARAM_MEAL_NAME = "s"
    }
}