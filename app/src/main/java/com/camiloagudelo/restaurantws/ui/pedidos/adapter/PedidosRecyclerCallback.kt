package com.camiloagudelo.restaurantws.ui.pedidos.adapter

import com.camiloagudelo.restaurantws.data.pedidos.models.Pedido

interface PedidosRecyclerCallback {
    fun remove(item: Pedido, position: Int)
    fun onClickItem(item: Pedido)
}