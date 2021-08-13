package com.camiloagudelo.restaurantws.ui.sign_up

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.data.auth.models.SignUpClient
import com.camiloagudelo.restaurantws.databinding.ActivitySignUpBinding
import com.camiloagudelo.restaurantws.ui.auth.BaseAuthActivity
import com.camiloagudelo.restaurantws.utils.afterTextChanged
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : BaseAuthActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val signUpViewModel: SignUpViewModel by viewModel()

    override fun initializeBinding() {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun observeFormState() {
        signUpViewModel.signUpFormState.observe(this@SignUpActivity, Observer {
            val loginState = it ?: return@Observer

            binding.apply {
                btnSignUp.isEnabled = loginState.isDataValid

                if (loginState.emailError != null) {
                    signUpEmail.error = getString(loginState.emailError)
                }

                if (loginState.passwordError != null) {
                    signUpPassword.error = getString(loginState.passwordError)
                }

                if (loginState.nameError != null) {
                    name.error = getString(loginState.nameError)
                }

                if (loginState.cityError != null) {
                    ciudad.error = getString(loginState.cityError)
                }
            }
        })
    }

    override fun observeAuthAction() {
        lifecycleScope.launchWhenStarted {
            signUpViewModel.signUpResult.collect {
                when (it) {
                    is Resource.Empty -> {

                    }
                    is Resource.Error -> {
                        showActionFailed(it.error?.message ?: "Error inesperado")
                        binding.signUpLoading.visibility = View.GONE
                        binding.btnSignUp.visibility = View.VISIBLE
                    }
                    is Resource.Loading -> {
                        binding.signUpLoading.visibility = View.VISIBLE
                        binding.btnSignUp.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        binding.signUpLoading.visibility = View.GONE
                        binding.btnSignUp.visibility = View.VISIBLE
                        showActionFailed(it.data!!.toString())
                    }
                }
            }
        }

    }

    override fun setUpInputsValidations() {
        binding.apply {
            setInputAfterTextChanged(listOf(signUpEmail, name, signUpPassword, ciudad))
            ciudad.apply {
                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE ->
                            signUpViewModel.signUpClient(buildSignUpClient())
                    }
                    false
                }

                btnSignUp.setOnClickListener {
                    signUpViewModel.signUpClient(buildSignUpClient())
                }

            }
        }
    }

    private fun setInputAfterTextChanged(inputs: List<EditText>) {
        inputs.forEach {
            it.afterTextChanged {
                signUpViewModel.loginDataChanged(buildSignUpClient())
            }
        }
    }

    private fun buildSignUpClient(): SignUpClient = with(binding) {
        SignUpClient(
            nombre = name.text.toString(),
            correo = signUpEmail.text.toString(),
            contrasena = signUpPassword.text.toString(),
            ciudad = ciudad.text.toString(),
        )
    }
}