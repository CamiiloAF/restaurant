package com.camiloagudelo.restaurantws.data.auth.models

data class LoggedInUser(
    val idCliente: Int,
    val nombre: String,
    val respuesta: String,
    val token: String
)