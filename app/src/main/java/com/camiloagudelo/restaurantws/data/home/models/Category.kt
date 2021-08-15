package com.camiloagudelo.restaurantws.data.home.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val descripcion: String,
    val id: Int,
    val nombre: String,
    val url_imagen: String,
) : Parcelable