package com.camiloagudelo.restaurantws.ui.pedidos.adapter

import com.camiloagudelo.restaurantws.data.pedidos.models.Pedido

interface PedidosRecyclerCallback {
    suspend fun remove(item: Pedido, position: Int)
}