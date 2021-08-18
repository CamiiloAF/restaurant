package com.camiloagudelo.restaurantws.ui.pedido_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.databinding.PedidoDetailFragmentBinding
import com.camiloagudelo.restaurantws.ui.pedido_detail.adapter.PedidoDetailProductsAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class PedidoDetailFragment : Fragment() {
    private var _binding: PedidoDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var rcViewAdapter: PedidoDetailProductsAdapter

    private val args by navArgs<PedidoDetailFragmentArgs>()

    private val pedidoDetailViewModel: PedidoDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PedidoDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRcView()
        setUpCardInfo()
    }

    private fun setUpCardInfo() {
        with(binding) {
            "$ ${(args.pedido.total * .19).roundToInt()}".also { txtIVA.text = it }
            "$ ${args.pedido.total}".also { txtTotalForPayment.text = it }
            btnSendPedido.setOnClickListener {
                pedidoDetailViewModel.sendPedido(args.pedido)
                handleSendPedidoResponse()
            }
        }
    }

    private fun setUpRcView() {
        rcViewAdapter = PedidoDetailProductsAdapter(args.pedido.products)

        with(binding) {
            rcViewPedidosProductsDetail.apply {
                adapter = rcViewAdapter
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                setHasFixedSize(true)
            }
        }
    }

    private fun handleSendPedidoResponse() {
        lifecycleScope.launchWhenStarted {
            pedidoDetailViewModel.sendPedidoResult.collect {
                when (it) {
                    is Resource.Empty -> {
                    }
                    is Resource.Error -> binding.apply {
                        pedidoDetailProgressBar.isVisible = false
                        rcViewPedidosProductsDetail.isVisible = true
                        cardView.isVisible = true
                    }
                    is Resource.Loading -> binding.apply {
                        pedidoDetailProgressBar.isVisible = true
                        rcViewPedidosProductsDetail.isVisible = false
                        cardView.isVisible = false
                    }

                    is Resource.Success -> {
                        MaterialAlertDialogBuilder(requireContext()).apply {
                            setTitle("El pedido fue enviado")
                            setMessage(it.data!!.mensaje)
                            setPositiveButton(android.R.string.ok) { _, _ ->
                                findNavController().popBackStack()
                            }
                            show()
                        }
                    }
                }
            }
        }
    }

}