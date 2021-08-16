package com.camiloagudelo.restaurantws.data.pedidos.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.camiloagudelo.restaurantws.data.products.models.Product
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import java.util.*

@Entity(tableName = "pedidos")
data class Pedido(
    @PrimaryKey(autoGenerate = true)
    @Expose(serialize = false)
    var id: Int?,
    val id_cliente: Int,
    val json_pedido: String,
    val total: Int,
    @Expose(serialize = false)
    val created_at: Date,
    @Expose(serialize = false)
    val canDelete: Boolean = false,
    @Expose(serialize = false)
    val products: List<Product>,
)