package com.camiloagudelo.restaurantws.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.camiloagudelo.restaurantws.data.home.models.Category
import com.camiloagudelo.restaurantws.databinding.CategoryItemBinding
import com.camiloagudelo.restaurantws.utils.load

class CategoriesRecyclerAdapter(
    private var items: List<Category>,
    private val onClickCategory: (category: Category) -> Unit,
) :
    RecyclerView.Adapter<CategoriesRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) = with(binding) {
            txtCategory.text = category.nombre
            txtCategoryDescription.text = category.descripcion

            root.setOnClickListener { onClickCategory(category) }

            imgCategory.load(category.url_imagen)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Category>) {
        items = list
        notifyDataSetChanged()
    }
}