package com.camiloagudelo.restaurantws.data.home.repositories

import com.camiloagudelo.restaurantws.core.api.ApiService
import com.camiloagudelo.restaurantws.data.home.models.CategoriesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HomeRepository(private val apiService: ApiService) {
    suspend fun getCategories(): Flow<CategoriesResponse> = flow {
        emit(apiService.getCategories())
    }.flowOn(Dispatchers.IO)
}