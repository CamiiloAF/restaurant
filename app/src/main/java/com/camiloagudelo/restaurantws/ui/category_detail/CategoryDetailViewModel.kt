package com.camiloagudelo.restaurantws.ui.category_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.data.products.ProductRepository
import com.camiloagudelo.restaurantws.data.products.models.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CategoryDetailViewModel(private val productRepository: ProductRepository) : ViewModel() {
    private val _productsResult = MutableStateFlow<Resource<List<Product>>>(Resource.Empty())
    val productsResult: StateFlow<Resource<List<Product>>> = _productsResult

    fun getProductsByCategory(categoryId: Int) {
        viewModelScope.launch {
            productRepository.getProductsByCategory(categoryId)
                .onStart { _productsResult.value = Resource.Loading() }
                .catch { e -> _productsResult.value = Resource.Error(e) }
                .collect { response ->
                    _productsResult.value =
                        Resource.Success(response.productos)
                }
        }
    }
}