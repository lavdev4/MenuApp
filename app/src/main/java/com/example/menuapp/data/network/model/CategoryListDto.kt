package com.example.menuapp.data.network.model

import com.google.gson.annotations.SerializedName

data class CategoryListDto(
    @SerializedName("categories")
    val categoriesContainers: List<CategoryDto>?
)