package com.camiloagudelo.restaurantws.ui.home.adapter

import com.camiloagudelo.restaurantws.data.home.models.Category

interface CategoriesRecyclerCallback {
    fun onClickCategory(category: Category)
}