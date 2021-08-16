package com.camiloagudelo.restaurantws.data.products

import com.camiloagudelo.restaurantws.core.api.ApiService
import com.camiloagudelo.restaurantws.data.pedidos.PedidosDao
import com.camiloagudelo.restaurantws.data.products.models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductRepository(private val apiService: ApiService, private val pedidosDao: PedidosDao) {
    suspend fun getProductsByCategory(categoryId: Int): Flow<List<Product>> = flow {
        val response = apiService.getProductsByCategory(categoryId)
        val pedido = pedidosDao.getOne().first()
        val finalProducts = mutableListOf<Product>()

        if (pedido != null) {
            pedido.products
            response.productos.forEach { e ->
                val index = pedido.products.indexOfFirst { p -> e.id == p.id }
                if (index != -1) {
                    e.quantity = pedido.products[index].quantity
                }

                finalProducts.add(e)
            }
        }

        emit(response.productos)
    }.flowOn(Dispatchers.IO)
}