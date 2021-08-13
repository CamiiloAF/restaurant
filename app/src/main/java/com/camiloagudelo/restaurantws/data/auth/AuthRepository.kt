package com.camiloagudelo.restaurantws.data.auth

import com.camiloagudelo.restaurantws.core.models.ApiResponse
import com.camiloagudelo.restaurantws.data.auth.models.LoggedInUser
import com.camiloagudelo.restaurantws.data.auth.models.SignUpClient
import kotlinx.coroutines.flow.Flow

class AuthRepository(private val dataSource: AuthDataSource) {
    suspend fun login(email: String, password: String): Flow<LoggedInUser?> {
        return dataSource.login(email, password)
    }

    suspend fun signUpClient(signUpClient: SignUpClient): Flow<ApiResponse> {
        return dataSource.signUpClient(signUpClient)
    }
}