package com.camiloagudelo.restaurantws.ui.pedido_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.core.models.ApiResponse
import com.camiloagudelo.restaurantws.data.pedidos.PedidosRepository
import com.camiloagudelo.restaurantws.data.pedidos.models.Pedido
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PedidoDetailViewModel(private val pedidosRepository: PedidosRepository) : ViewModel() {
    private val _sendResult = MutableStateFlow<Resource<ApiResponse>>(Resource.Empty())
    val sendPedidoResult: StateFlow<Resource<ApiResponse>> = _sendResult

    fun sendPedido(pedido: Pedido) {
        viewModelScope.launch {
            pedidosRepository.sendPedido(pedido.apply { setJsonPedido() })
                .onStart { _sendResult.value = Resource.Loading() }
                .catch { e -> _sendResult.value = Resource.Error(e) }
                .collect { response ->
                    _sendResult.value = Resource.Success(response)
                }
        }
    }
}