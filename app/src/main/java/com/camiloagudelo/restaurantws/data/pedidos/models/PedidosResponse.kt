package com.camiloagudelo.restaurantws.data.pedidos.models

data class PedidosResponse(
    val pedidos: List<Pedido>,
    val respuesta: String,
)