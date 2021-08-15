package com.camiloagudelo.restaurantws.ui.category_detail.adapter

import com.camiloagudelo.restaurantws.data.products.models.Product

interface ProductsRecyclerCallback{
    fun onAddToCar(item: Product, position: Int)
    fun onRemoveFromCar(item: Product, position: Int)
    fun onClickItem(item: Product)
}