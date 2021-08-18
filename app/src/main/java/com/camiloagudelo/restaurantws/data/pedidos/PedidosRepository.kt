package com.camiloagudelo.restaurantws.data.pedidos

import com.camiloagudelo.restaurantws.core.api.ApiService
import com.camiloagudelo.restaurantws.core.models.ApiResponse
import com.camiloagudelo.restaurantws.data.auth.models.CurrentUser
import com.camiloagudelo.restaurantws.data.pedidos.models.Pedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PedidosRepository(private val apiService: ApiService, private val pedidosDao: PedidosDao) {
    fun getLocalPedidos(): Flow<List<Pedido>> = pedidosDao.getAll()

    suspend fun getPedidosByClient(currentUser: CurrentUser): Flow<MutableList<Pedido>> = flow {
        emit(apiService.getPedidosByClient(currentUser).pedidos.toMutableList())
    }.flowOn(Dispatchers.IO)

    fun getPedido(): Flow<Pedido?> = pedidosDao.getOne()

    suspend fun deletePedido(pedido: Pedido): Flow<Unit> = flow {
        emit(pedidosDao.deleteByUserId(pedido.id!!))
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

    suspend fun sendPedido(pedido: Pedido): Flow<ApiResponse> = flow {
        val response = apiService.sendPedido(pedido.copy(id = null))
        pedidosDao.deleteByUserId(pedido.id!!)
        emit(response)
    }
}