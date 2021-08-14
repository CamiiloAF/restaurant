package com.camiloagudelo.restaurantws.domain

import com.google.gson.Gson

data class LoginRequest(
    val email: String, val password: String,
) {
    fun toJson(): String = Gson().toJson(this)

    companion object {
        fun fromJson(json: String?): LoginRequest? = json?.let { Gson().fromJson(it, LoginRequest::class.java) }
    }
}
