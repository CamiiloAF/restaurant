package com.camiloagudelo.restaurantws.data.auth

import com.camiloagudelo.restaurantws.core.models.ApiResponse
import com.camiloagudelo.restaurantws.data.auth.models.LoggedInUser
import com.camiloagudelo.restaurantws.data.auth.models.SignUpClient
import com.camiloagudelo.restaurantws.domain.LoginRequest
import kotlinx.coroutines.flow.Flow

class AuthRepository(private val dataSource: AuthDataSource) {
    suspend fun login(loginRequest: LoginRequest): Flow<LoggedInUser?> {
        return dataSource.login(loginRequest)
    }

    suspend fun signUpClient(signUpClient: SignUpClient): Flow<ApiResponse> {
        return dataSource.signUpClient(signUpClient)
    }
}