package com.camiloagudelo.restaurantws.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.data.home.models.Category
import com.camiloagudelo.restaurantws.data.home.repositories.HomeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {
    private val _categoriesResult = MutableStateFlow<Resource<List<Category>>>(Resource.Empty())
    val categoriesResult: StateFlow<Resource<List<Category>>> = _categoriesResult

    fun getCategories() {
        if(_categoriesResult.value is Resource.Success) return
        viewModelScope.launch {
            homeRepository.getCategories()
                .onStart { _categoriesResult.value = Resource.Loading() }
                .catch { e -> _categoriesResult.value = Resource.Error(e) }
                .collect { response ->
                    _categoriesResult.value =
                        Resource.Success(response.categories)
                }
        }
    }

}