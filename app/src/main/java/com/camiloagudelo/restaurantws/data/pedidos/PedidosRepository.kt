package com.camiloagudelo.restaurantws.data.pedidos

import com.camiloagudelo.restaurantws.core.api.ApiService
import com.camiloagudelo.restaurantws.data.auth.models.CurrentUser
import com.camiloagudelo.restaurantws.data.pedidos.models.Pedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PedidosRepository(private val apiService: ApiService, private val pedidosDao: PedidosDao) {
    suspend fun getPedidosByClient(currentUser: CurrentUser): Flow<List<Pedido>> = flow {
        val localPedidos = pedidosDao.getAll().first().toMutableList()
        val remotePedidos = apiService.getPedidosByClient(currentUser)

        localPedidos.addAll(remotePedidos.pedidos)

        emit(localPedidos)
    }.flowOn(Dispatchers.IO)

    fun getPedido(): Flow<Pedido?> = pedidosDao.getOne()

    suspend fun deletePedido(pedido: Pedido): Flow<Unit> = flow {
        emit(pedidosDao.delete(pedido))
    }

    suspend fun savePedido(pedido: Pedido): Flow<Pedido> = flow {
        if (pedido.id == null) {
            val id = pedidosDao.insert(pedido)
            pedido.id = id.toInt()
        } else {
            pedidosDao.update(pedido)
        }

        emit(pedido)
    }
}