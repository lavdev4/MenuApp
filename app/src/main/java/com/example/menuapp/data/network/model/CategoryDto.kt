package com.example.menuapp.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("idCategory")
    @Expose
    val id: Int?,
    @SerializedName("strCategory")
    @Expose
    val name: String?
)
