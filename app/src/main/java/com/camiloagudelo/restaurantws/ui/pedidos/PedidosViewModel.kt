package com.camiloagudelo.restaurantws.ui.pedidos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.data.pedidos.PedidosRepository
import com.camiloagudelo.restaurantws.data.pedidos.models.Pedido
import com.camiloagudelo.restaurantws.ui.login.CurrentUser
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PedidosViewModel(private val pedidosRepository: PedidosRepository) : ViewModel() {
    private val _pedidosResult = MutableStateFlow<Resource<List<Pedido>>>(Resource.Empty())
    val pedidosResult: StateFlow<Resource<List<Pedido>>> = _pedidosResult

    private val _deletePedidoResult = MutableStateFlow<Resource<Unit>>(Resource.Empty())
    val deletePedidoResult: StateFlow<Resource<Unit>> = _deletePedidoResult

    fun getPedidos(currentUser: CurrentUser) {
        if (_pedidosResult.value is Resource.Success) return
        viewModelScope.launch {
            pedidosRepository.getPedidosByClient(currentUser)
                .onStart { _pedidosResult.value = Resource.Loading() }
                .catch { e -> _pedidosResult.value = Resource.Error(e) }
                .collect { response ->
                    _pedidosResult.value = Resource.Success(response)
                }
        }
    }

    fun deletePedido(pedido: Pedido) {
        if (_deletePedidoResult.value is Resource.Loading) return

        viewModelScope.launch {
            pedidosRepository.deletePedido(pedido)
                .onStart { _deletePedidoResult.value = Resource.Loading() }
                .catch { e -> _deletePedidoResult.value = Resource.Error(e) }
                .collect { res ->
                    _deletePedidoResult.value = Resource.Success(res)
                }
        }
    }


}