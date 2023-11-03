package com.example.menuapp.data

import com.example.menuapp.data.database.MealInfoDao
import com.example.menuapp.data.mappers.MealsMapper
import com.example.menuapp.data.network.ApiService
import com.example.menuapp.di.annotations.ApplicationScope
import com.example.menuapp.domain.Repository
import com.example.menuapp.domain.entities.MealEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@ApplicationScope
class RepositoryImpl @Inject constructor(
    private val dao: MealInfoDao,
    private val apiService: ApiService,
    private val mapper: MealsMapper,
) : Repository {

    override fun getMealCategories(): Flow<List<String>> {
        return dao.getMealCategories()
    }

    override fun getMealsByCategory(category: String): Flow<List<MealEntity>> {
        return dao.getMealsByCategory(category).map { dbModels ->
            dbModels.map { mapper.mapMealDbModelToMealEntity(it) }
        }
    }

    override fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val meals = apiService.getCategories().categoriesContainers
                ?.mapNotNull { it.name }
                ?.let { categories ->
                    val mealNames = categories.mapNotNull {
                        apiService.getMealsByCategory(it).mealContainers
                    }.flatten().mapNotNull { it.name }
                    val meals = mealNames.mapNotNull {
                        apiService.getMealInfo(it).mealInfoContainers
                    }.flatten()
                    meals
                }
            meals?.mapNotNull { mapper.mapMealInfoDtoToMealDbModel(it) }?.let {
                dao.insert(it)
            }
        }
    }
}