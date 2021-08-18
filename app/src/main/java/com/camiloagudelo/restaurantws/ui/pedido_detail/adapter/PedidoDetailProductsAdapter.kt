package com.camiloagudelo.restaurantws.ui.pedido_detail.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.camiloagudelo.restaurantws.data.products.models.Product
import com.camiloagudelo.restaurantws.databinding.PedidoProductDetailItemBinding
import com.camiloagudelo.restaurantws.utils.load

class PedidoDetailProductsAdapter(
    private var items: List<Product>,
) :
    RecyclerView.Adapter<PedidoDetailProductsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: PedidoProductDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) = with(binding) {
            "$ ${product.precio}".also { txtSinglePrice.text = it }
            txtQuantity.text = product.quantity.toString()
            "$ ${product.precio * product.quantity}".also { txtProductTotal.text = it }

            imgPedidoDetailProduct.load(product.url_imagen)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PedidoProductDetailItemBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Product>) {
        items = list
        notifyDataSetChanged()
    }
}