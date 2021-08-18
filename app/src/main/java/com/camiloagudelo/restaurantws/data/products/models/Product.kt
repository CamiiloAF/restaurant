package com.camiloagudelo.restaurantws.data.products.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val descripcion: String,
    val id: Int,
    val nombre: String,
    val precio: Int,
    val url_imagen: String,
    @Expose(deserialize = false)
    var quantity: Int = 0,
) : Parcelable {
    fun toJsonPedido(): String {
        return "{ \"id_producto\": $id, \"cantidad\":$quantity, \"precio\":$precio } "
    }
}