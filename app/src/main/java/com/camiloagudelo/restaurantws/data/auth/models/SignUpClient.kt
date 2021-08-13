package com.camiloagudelo.restaurantws.data.auth.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpClient(
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val ciudad: String,
) : Parcelable