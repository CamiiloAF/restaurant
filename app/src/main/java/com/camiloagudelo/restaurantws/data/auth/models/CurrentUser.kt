package com.camiloagudelo.restaurantws.data.auth.models

object CurrentUser {
    var idCliente: Int = -1
    lateinit var nombre: String
    lateinit var token: String
}