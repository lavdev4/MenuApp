package com.example.menuapp.data

import com.example.menuapp.data.database.MealInfoDao
import com.example.menuapp.data.mappers.MealsMapper
import com.example.menuapp.data.network.ApiService
import com.example.menuapp.di.annotations.ApplicationScope
import com.example.menuapp.domain.Repository
import com.example.menuapp.domain.entities.CategoryEntity
import com.example.menuapp.domain.entities.MealEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ApplicationScope
class RepositoryImpl @Inject constructor(
    private val dao: MealInfoDao,
    private val apiService: ApiService,
    private val mapper: MealsMapper,
) : Repository {

    override fun getMealCategories(): Flow<List<CategoryEntity>> {
        return dao.getMealCategories().map { dbModels ->
            dbModels.map { mapper.mapCategoryStrToCategoryEntity(it) }
        }
    }

    override suspend fun getMealsByCategory(category: String): List<MealEntity> {
        return dao.getMealsByCategory(category).map { dbModels ->
            mapper.mapMealDbModelToMealEntity(dbModels)
        }
    }

    override suspend fun loadData() {
        apiService.getCategories().categoriesContainers
            ?.filter { it.name != null }
            ?.take(7)
            ?.sortedBy { it.name }
            ?.mapNotNull { it.name }
            ?.forEach { category ->
                apiService.getMealsByCategory(category).mealContainers
                    ?.mapNotNull { it.name }
                    ?.mapNotNull { apiService.getMealInfo(it).mealInfoContainers }
                    ?.flatten()
                    ?.mapNotNull { mapper.mapMealInfoDtoToMealDbModel(it) }
                    ?.let { dao.insert(it) }
            }
    }

    override suspend fun deleteData() {
        dao.deleteAllData()
    }
}