package com.example.menuapp.data.network.model

import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("idCategory")
    val id: Int?,
    @SerializedName("strCategory")
    val name: String?
)
