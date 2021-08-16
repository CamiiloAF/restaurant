package com.camiloagudelo.restaurantws.ui.specialty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.data.specialty.models.Speciality
import com.camiloagudelo.restaurantws.databinding.SpecialtyActivityBinding
import com.camiloagudelo.restaurantws.ui.MainActivity
import com.camiloagudelo.restaurantws.utils.goToActivity
import com.camiloagudelo.restaurantws.utils.load
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class SpecialtyActivity : AppCompatActivity() {

    lateinit var binding: SpecialtyActivityBinding

    private val homeViewModel: SpecialtyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SpecialtyActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel.getSpeciality()
        handleSpecialityResponse()
    }

    private fun handleSpecialityResponse() {
        lifecycleScope.launchWhenStarted {
            homeViewModel.specialityResult.collect {
                when (it) {
                    is Resource.Empty -> {
                    }
                    is Resource.Error -> binding.apply {
                        specialityProgressBar.isVisible = false
                        goToHome()
                    }
                    is Resource.Loading -> binding.apply {
                        specialityProgressBar.isVisible = true
                    }

                    is Resource.Success -> {
                        binding.apply {
                            specialityProgressBar.isVisible = false
                        }

                        bindData(it.data)
                    }
                }
            }
        }
    }


    private fun bindData(data: Speciality?) {
        with(binding) {
            txtSpecialityName.text = data?.nombre
            txtSpecialityDescription.text = data?.descripcion
            "$ ${data?.precio}".also { txtSpecialityPrice.text = it }

            data?.url_foto?.let { imgSpeciality.load(it) }
            btnContinue.setOnClickListener { goToHome() }
        }
    }

    private fun goToHome() {
        goToActivity<MainActivity>()
        finish()
    }
}