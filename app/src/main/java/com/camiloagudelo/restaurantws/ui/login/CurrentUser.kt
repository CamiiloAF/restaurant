package com.camiloagudelo.restaurantws.ui.login

import com.google.gson.Gson

data class CurrentUser(
    val token: String,
    val clientID: Int,
    val nombre: String,
) {
    fun toJson(): String = Gson().toJson(this)

    companion object {
        fun fromJson(json: String?): CurrentUser? =
            json?.let { Gson().fromJson(it, CurrentUser::class.java) }
    }
}