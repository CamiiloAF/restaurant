package com.camiloagudelo.restaurantws.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.data.auth.models.CurrentUser
import com.camiloagudelo.restaurantws.data.pedidos.PedidosRepository
import com.camiloagudelo.restaurantws.data.pedidos.models.Pedido
import com.camiloagudelo.restaurantws.data.products.models.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(private val pedidosRepository: PedidosRepository) : ViewModel() {
    private val _pedidoResult = MutableStateFlow<Resource<Pedido>>(Resource.Empty())

    private val _insertPedidoResult = MutableStateFlow<Resource<Pedido>>(Resource.Empty())
    val insertPedidoResult: StateFlow<Resource<Pedido>> = _insertPedidoResult

    fun getPedido() {
        if (_pedidoResult.value is Resource.Success) return
        viewModelScope.launch {
            pedidosRepository.getPedido()
                .catch { e -> e.printStackTrace() }
                .collect { response ->
                    _pedidoResult.value = Resource.Success(response ?: buildNewPedido())
                }
        }
    }

    private fun buildNewPedido(): Pedido {
        return Pedido(
            null,
            CurrentUser.idCliente,
            "",
            0,
            Date(),
            true,
            mutableListOf()
        )
    }


    fun insertOrUpdatePedidoWithProduct(product: Product) {
        val pedido = _pedidoResult.value.data!!
        val updatedProducts = getUpdatedProducts(pedido.products.toMutableList(), product)
        val total = updatedProducts.sumOf { p -> p.precio * p.quantity }

        viewModelScope.launch {
            pedidosRepository.savePedido(pedido.copy(products = updatedProducts, total = total ))
                .onStart { _insertPedidoResult.value = Resource.Loading() }
                .catch { e -> _insertPedidoResult.value = Resource.Error(e) }
                .collect { response ->
                    _insertPedidoResult.value = Resource.Success(response)
                }
        }
    }

    private fun getUpdatedProducts(
        products: MutableList<Product>,
        product: Product,
    ): MutableList<Product> {
        val index = products.indexOfFirst { p -> p.id == product.id }

        if (index != -1) {
            if (product.quantity == 0) {
                products.removeAt(index)
            } else {
                products[index] = product
            }
        } else {
            products.add(product)
        }

        return products
    }

}