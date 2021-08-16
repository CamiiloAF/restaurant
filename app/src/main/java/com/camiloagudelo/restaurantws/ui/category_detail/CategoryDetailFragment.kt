package com.camiloagudelo.restaurantws.ui.category_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.data.products.models.Product
import com.camiloagudelo.restaurantws.databinding.CategoryDetailFragmentBinding
import com.camiloagudelo.restaurantws.ui.MainViewModel
import com.camiloagudelo.restaurantws.ui.category_detail.adapter.ProductsRecyclerAdapter
import com.camiloagudelo.restaurantws.ui.category_detail.adapter.ProductsRecyclerCallback
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class CategoryDetailFragment : Fragment() {

    private var _binding: CategoryDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val categoryDetailViewModel: CategoryDetailViewModel by viewModel()
    private val mainViewModel: MainViewModel by sharedViewModel()

    private lateinit var rcViewAdapter: ProductsRecyclerAdapter

    private val args by navArgs<CategoryDetailFragmentArgs>()

    private val productsRecyclerCallback = object : ProductsRecyclerCallback {
        override fun onAddToCar(item: Product, position: Int) {
            item.quantity++
            mainViewModel.insertOrUpdatePedidoWithProduct(item)
            observePedido(item, position)
        }

        override fun onRemoveFromCar(item: Product, position: Int) {
            if(item.quantity == 0) return

            item.quantity--
            mainViewModel.insertOrUpdatePedidoWithProduct(item)
            observePedido(item, position)
        }

        override fun onClickItem(item: Product) {
        }

        private fun observePedido(
            item: Product,
            position: Int,
        ) {
            lifecycleScope.launchWhenStarted {
                mainViewModel.insertPedidoResult.collect {
                    when (it) {
                        is Resource.Empty -> {
                        }
                        is Resource.Error -> {
                        }
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            rcViewAdapter.updateItem(item, position)
                            this.cancel()
                        }
                    }
                }
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = CategoryDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRcView()
        setHeaderTexts()
        categoryDetailViewModel.getProductsByCategory(args.category.id)
        handleProductsResponse()
    }

    private fun setUpRcView() {
        rcViewAdapter = ProductsRecyclerAdapter(mutableListOf(),
            productsRecyclerCallback)

        binding.rcViewProducts.apply {
            adapter = rcViewAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setHeaderTexts() {
        with(binding) {
            categoryDetailHeader.txtHeaderTitle.text = args.category.nombre
            categoryDetailHeader.txtHeaderDescription.text = args.category.descripcion
        }
    }

    private fun handleProductsResponse() {
        lifecycleScope.launchWhenStarted {
            categoryDetailViewModel.productsResult.collect {
                when (it) {
                    is Resource.Empty -> {
                    }
                    is Resource.Error -> binding.apply {
                        categoryDetailProgressBar.isVisible = false
                        rcViewProducts.isVisible = false
                    }
                    is Resource.Loading -> binding.apply {
                        categoryDetailProgressBar.isVisible = true
                        rcViewProducts.isVisible = false
                    }

                    is Resource.Success -> {
                        binding.apply {
                            categoryDetailProgressBar.isVisible = false
                            rcViewProducts.isVisible = true
                        }

                        rcViewAdapter.setItems(it.data ?: arrayListOf())
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}