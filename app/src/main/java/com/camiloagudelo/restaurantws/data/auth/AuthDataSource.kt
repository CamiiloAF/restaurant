package com.camiloagudelo.restaurantws.data.auth

import com.camiloagudelo.restaurantws.core.api.ApiService
import com.camiloagudelo.restaurantws.core.models.ApiResponse
import com.camiloagudelo.restaurantws.data.auth.models.LoggedInUser
import com.camiloagudelo.restaurantws.data.auth.models.SignUpClient
import com.camiloagudelo.restaurantws.domain.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthDataSource(private val apiService: ApiService) {
    suspend fun login(loginRequest: LoginRequest): Flow<LoggedInUser?> = flow {
        emit(apiService.login(loginRequest.email, loginRequest.password))
    }.flowOn(Dispatchers.IO)

    suspend fun signUpClient(signUpClient: SignUpClient): Flow<ApiResponse> = flow {
        emit(apiService.signUpClient(signUpClient))
    }.flowOn(Dispatchers.IO)
}