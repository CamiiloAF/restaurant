package com.camiloagudelo.restaurantws.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.camiloagudelo.restaurantws.data.home.models.Category
import com.camiloagudelo.restaurantws.databinding.CategoryItemBinding

class CategoriesRecyclerAdapter(private var items: List<Category>) :
    RecyclerView.Adapter<CategoriesRecyclerAdapter.ViewHolder>() {

    lateinit var categoriesRecyclerCallback: CategoriesRecyclerCallback

    inner class ViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) = with(binding) {
            txtCategory.text = category.nombre
            txtCategoryDescription.text = category.descripcion

            root.setOnClickListener { categoriesRecyclerCallback.onClickCategory(category) }

            Glide.with(root).load(category.url_imagen)
                .into(imgCategory)
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