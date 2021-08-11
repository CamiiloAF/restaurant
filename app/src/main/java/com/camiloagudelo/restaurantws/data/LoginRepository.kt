package com.camiloagudelo.restaurantws.data

import com.camiloagudelo.restaurantws.data.model.LoggedInUser
import kotlinx.coroutines.flow.Flow

class LoginRepository(private val dataSource: LoginDataSource) {
    fun login(email: String, password: String): Flow<LoggedInUser> {
        return dataSource.login(email, password)
    }
}