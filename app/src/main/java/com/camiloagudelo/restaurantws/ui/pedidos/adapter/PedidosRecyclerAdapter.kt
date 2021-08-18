package com.camiloagudelo.restaurantws.ui.pedidos.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.camiloagudelo.restaurantws.data.auth.models.CurrentUser
import com.camiloagudelo.restaurantws.data.auth.models.LoggedInUser
import com.camiloagudelo.restaurantws.data.pedidos.models.Pedido
import com.camiloagudelo.restaurantws.databinding.PedidoItemBinding
import kotlinx.coroutines.runBlocking

class PedidosRecyclerAdapter(
    private var items: MutableList<Pedido>,
    private val pedidosRecyclerCallback: PedidosRecyclerCallback,
) :
    RecyclerView.Adapter<PedidosRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: PedidoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) = with(binding) {
            val pedido = items[position]

            imgDeletePedido.apply {
                if (pedido.canDelete) {
                    isVisible = true
                    setOnClickListener { pedidosRecyclerCallback.remove(pedido, position) }
                } else {
                    isVisible = false
                }
            }

            root.setOnClickListener { pedidosRecyclerCallback.onClickItem(pedido) }

            txtPedidoDate.text = pedido.created_at.toString()
            txtTotal.text = pedido.total.toString()
            txtClientName.text = CurrentUser.nombre
            txtNumberOfPedido.text = pedido.id.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PedidoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Pedido>) {
        items = list.toMutableList()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}