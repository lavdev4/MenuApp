package com.example.menuapp.data.network.model

import com.google.gson.annotations.SerializedName

data class HttpErrorDto(
    val message: String,
    @SerializedName("error")
    val code: Int
)