package com.camiloagudelo.restaurantws.data

import com.camiloagudelo.restaurantws.core.api.ApiService
import com.camiloagudelo.restaurantws.data.model.LoggedInUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(private val apiService: ApiService) {

    fun login(email: String, password: String): Flow<LoggedInUser> = flow {
        emit(apiService.login(email, password))
    }.flowOn(Dispatchers.IO)

    fun logout() {
        // TODO: revoke authentication
    }
}