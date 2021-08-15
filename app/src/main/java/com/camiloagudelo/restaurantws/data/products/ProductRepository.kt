package com.camiloagudelo.restaurantws.data.products

import com.camiloagudelo.restaurantws.core.api.ApiService
import com.camiloagudelo.restaurantws.data.products.models.ProductsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductRepository(private val apiService: ApiService) {
    suspend fun getProductsByCategory(categoryId: Int): Flow<ProductsResponse> = flow {
        emit(apiService.getProductsByCategory(categoryId))
    }.flowOn(Dispatchers.IO)
}