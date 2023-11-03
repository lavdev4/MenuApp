package com.example.menuapp.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CategoryListDto(
    @SerializedName("categories")
    @Expose
    val categoriesContainers: List<CategoryDto>?
)