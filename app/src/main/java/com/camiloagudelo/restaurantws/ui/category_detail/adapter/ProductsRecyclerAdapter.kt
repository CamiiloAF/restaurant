package com.camiloagudelo.restaurantws.ui.category_detail.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.camiloagudelo.restaurantws.data.products.models.Product
import com.camiloagudelo.restaurantws.databinding.ProductItemBinding
import com.camiloagudelo.restaurantws.utils.load

class ProductsRecyclerAdapter(
    private var items: MutableList<Product>,
    private val productsRecyclerCallback: ProductsRecyclerCallback,
) :
    RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) = with(binding) {
            val product = items[position]

            imgProduct.load(product.url_imagen)
            txtProduct.text = product.nombre
            txtProductDescription.text = product.descripcion
            "$ ${product.precio}".also { txtProductPrice.text = it }

            txtProductQuantity.apply {
                if (product.quantity == 0) {
                    isVisible = false
                } else {
                    isVisible = true
                    text = product.quantity.toString()
                }
            }

            imgAddToCar.setOnClickListener {
                productsRecyclerCallback.onAddToCar(product, position)
            }

            imgRemoveFromCar.apply {
                if(txtProductQuantity.isVisible){
                    isVisible = true
                    setOnClickListener {
                        productsRecyclerCallback.onRemoveFromCar(product, position)
                    }
                }else{
                    isVisible = false
                }
            }

            root.setOnClickListener {
                productsRecyclerCallback.onClickItem(product)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Product>) {
        items = list.toMutableList()
        notifyDataSetChanged()
    }

    fun updateItem(item: Product, position: Int) {
        items[position] = item
        notifyItemChanged(position)
    }
}