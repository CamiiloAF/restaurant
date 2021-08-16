package com.camiloagudelo.restaurantws.core.database

import androidx.room.TypeConverter
import com.camiloagudelo.restaurantws.data.products.models.Product
import com.google.gson.Gson
import java.util.*

class Converters {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? = dateLong?.let { Date(it) }

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun productsToJson(products: List<Product>): String = Gson().toJson(products)

    @TypeConverter
    fun jsonToProducts(json: String): List<Product> {
        val x = Gson().fromJson(json, Array<Product>::class.java)
        return x.toList()
    }
}