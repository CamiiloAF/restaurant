package com.camiloagudelo.restaurantws.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.camiloagudelo.restaurantws.R
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.data.home.models.Category
import com.camiloagudelo.restaurantws.databinding.FragmentHomeBinding
import com.camiloagudelo.restaurantws.ui.category_detail.adapter.ProductsRecyclerAdapter
import com.camiloagudelo.restaurantws.ui.home.adapter.CategoriesRecyclerAdapter
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModel()

    lateinit var rcViewAdapter: CategoriesRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRcView()
        setHeaderTexts()
        homeViewModel.getCategories()
        handleCategoriesResponse()
    }

    private fun setHeaderTexts() {
        with(binding) {
            headerHome.txtHeaderTitle.text = getString(R.string.categories)
            headerHome.txtHeaderDescription.text = getString(R.string.home_header_desc)
        }
    }

    private fun handleCategoriesResponse() {
        lifecycleScope.launchWhenStarted {
            homeViewModel.categoriesResult.collect {
                when (it) {
                    is Resource.Empty -> {
                    }
                    is Resource.Error -> binding.apply {
                        homeProgressBar.isVisible = false
                        rcViewCategories.isVisible = false
                    }
                    is Resource.Loading -> binding.apply {
                        homeProgressBar.isVisible = true
                        rcViewCategories.isVisible = false
                    }

                    is Resource.Success -> {
                        binding.apply {
                            homeProgressBar.isVisible = false
                            rcViewCategories.isVisible = true
                        }

                        rcViewAdapter.setItems(it.data ?: arrayListOf())
                    }
                }
            }
        }
    }

    private fun setUpRcView() {
        rcViewAdapter = CategoriesRecyclerAdapter(listOf(), ::goToDetail)

        with(binding) {
            rcViewCategories.apply {
                adapter = rcViewAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
            }
        }
    }

    private fun goToDetail(category: Category) {
        val action = HomeFragmentDirections.actionFirstFragmentToCategoryDetailFragment(category)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}