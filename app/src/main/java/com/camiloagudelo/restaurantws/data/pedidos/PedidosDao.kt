package com.camiloagudelo.restaurantws.data.pedidos

import androidx.room.*
import com.camiloagudelo.restaurantws.data.pedidos.models.Pedido
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidosDao {
    @Query("SELECT * FROM pedidos")
    fun getAll(): Flow<List<Pedido>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pedido: Pedido)

    @Delete
    suspend fun delete(pedido: Pedido)
}