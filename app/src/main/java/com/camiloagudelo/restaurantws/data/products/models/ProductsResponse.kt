package com.camiloagudelo.restaurantws.data.products.models

data class ProductsResponse(
    val descripcion: String,
    val nombre: String,
    val productos: List<Product>,
    val respuesta: String
)