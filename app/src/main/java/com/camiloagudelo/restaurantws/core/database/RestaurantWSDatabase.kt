package com.camiloagudelo.restaurantws.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.camiloagudelo.restaurantws.data.pedidos.models.Pedido
import com.camiloagudelo.restaurantws.data.pedidos.PedidosDao

@Database(entities = [Pedido::class], version = 1, exportSchema = false)
 @TypeConverters(Converters::class)
abstract class RestaurantWSDatabase : RoomDatabase() {

    abstract fun pedidosDao(): PedidosDao

    companion object {
        @Volatile
        private var INSTANCE: RestaurantWSDatabase? = null

        fun getDatabase(context: Context): RestaurantWSDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RestaurantWSDatabase::class.java,
                    "restaurant_ws"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}