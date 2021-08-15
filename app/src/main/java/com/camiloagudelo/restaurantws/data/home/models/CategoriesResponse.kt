package com.camiloagudelo.restaurantws.data.home.models

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    val respuesta: String,
    @SerializedName("datos")
    val categories: List<Category>,
)