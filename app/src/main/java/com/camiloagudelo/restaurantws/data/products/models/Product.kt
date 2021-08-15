package com.camiloagudelo.restaurantws.data.products.models

import com.google.gson.annotations.Expose

data class Product(
    val descripcion: String,
    val id: Int,
    val nombre: String,
    val precio: Int,
    val url_imagen: String,
    @Expose(deserialize = false)
    var quantity: Int = 0
)