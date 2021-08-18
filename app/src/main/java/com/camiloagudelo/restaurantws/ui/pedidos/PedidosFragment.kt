package com.camiloagudelo.restaurantws.ui.pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.data.auth.models.CurrentUser
import com.camiloagudelo.restaurantws.data.pedidos.models.Pedido
import com.camiloagudelo.restaurantws.databinding.PedidosFragmentBinding
import com.camiloagudelo.restaurantws.ui.pedidos.adapter.PedidosRecyclerAdapter
import com.camiloagudelo.restaurantws.ui.pedidos.adapter.PedidosRecyclerCallback
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class PedidosFragment : Fragment() {

    private var _binding: PedidosFragmentBinding? = null
    private val binding get() = _binding!!

    private val pedidosViewModel: PedidosViewModel by viewModel()

    private lateinit var rcViewAdapter: PedidosRecyclerAdapter

    private val pedidosRecyclerCallback = object : PedidosRecyclerCallback {
        override fun remove(item: Pedido, position: Int) {
            pedidosViewModel.deletePedido(item)
            observeDeletePedido(position)
        }

        override fun onClickItem(item: Pedido) {
            val action = PedidosFragmentDirections.actionPedidosFragmentToPedidoDetailFragment(item)
            findNavController().navigate(action)
        }

        private fun observeDeletePedido(position: Int) {
            lifecycleScope.launchWhenStarted {
                pedidosViewModel.pedidosResult.collect {
                    when (it) {
                        is Resource.Empty -> {
                        }
                        is Resource.Error -> binding.apply {
                            Toast.makeText(context,
                                "No se pudo borrar el pedido",
                                Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> binding.apply {
                        }

                        is Resource.Success -> {
                            rcViewAdapter.removeItem(position)
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
        _binding = PedidosFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRcView()
        pedidosViewModel.getPedidos(CurrentUser)
        handleCategoriesResponse()
    }

    private fun setUpRcView() {
        rcViewAdapter = PedidosRecyclerAdapter(mutableListOf(), pedidosRecyclerCallback)

        binding.rcViewPedidos.apply {
            adapter = rcViewAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun handleCategoriesResponse() {
        lifecycleScope.launchWhenStarted {
            pedidosViewModel.pedidosResult.collect {
                when (it) {
                    is Resource.Empty -> {
                    }
                    is Resource.Error -> binding.apply {
                        pedidosDetailProgressBar.isVisible = false
                        rcViewPedidos.isVisible = false
                    }
                    is Resource.Loading -> binding.apply {
                        pedidosDetailProgressBar.isVisible = true
                        rcViewPedidos.isVisible = false
                    }

                    is Resource.Success -> {
                        binding.apply {
                            pedidosDetailProgressBar.isVisible = false
                            rcViewPedidos.isVisible = true
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