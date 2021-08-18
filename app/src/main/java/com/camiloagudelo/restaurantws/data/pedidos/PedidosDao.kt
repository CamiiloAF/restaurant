package com.camiloagudelo.restaurantws.data.pedidos

import androidx.room.*
import com.camiloagudelo.restaurantws.data.pedidos.models.Pedido
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidosDao {
    @Query("SELECT * FROM pedidos")
    fun getAll(): Flow<List<Pedido>>

    @Query("SELECT * FROM pedidos LIMIT 1")
    fun getOne(): Flow<Pedido?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pedido: Pedido): Long

    @Update
    suspend fun update(pedido: Pedido): Int

    @Query("DELETE FROM pedidos WHERE id = :pedidoId")
    suspend fun deleteByUserId(pedidoId: Int)
}