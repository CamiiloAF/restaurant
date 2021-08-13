package com.camiloagudelo.restaurantws.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.databinding.ActivityLoginBinding
import com.camiloagudelo.restaurantws.ui.auth.BaseAuthActivity
import com.camiloagudelo.restaurantws.ui.sign_up.SignUpActivity
import com.camiloagudelo.restaurantws.utils.afterTextChanged
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseAuthActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.txtSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    override fun initializeBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun observeFormState() {
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            binding.apply {
                login.isEnabled = loginState.isDataValid

                if (loginState.usernameError != null) {
                    loginEmail.error = getString(loginState.usernameError)
                }

                if (loginState.passwordError != null) {
                    password.error = getString(loginState.passwordError)
                }
            }
        })
    }

    override fun observeAuthAction() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginResult.collect {
                when (it) {
                    is Resource.Empty -> {

                    }
                    is Resource.Error -> {
                        showActionFailed(it.error?.message ?: "Error inesperado")
                        binding.loading.visibility = View.GONE
                    }
                    is Resource.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.loading.visibility = View.GONE
                        showActionFailed(it.data!!.toString())
                    }
                }
            }
        }
    }

    override fun setUpInputsValidations() {
        binding.apply {
            loginEmail.afterTextChanged {
                loginViewModel.loginDataChanged(
                    loginEmail.text.toString(),
                    password.text.toString()
                )
            }
            password.apply {
                afterTextChanged {
                    loginViewModel.loginDataChanged(
                        loginEmail.text.toString(),
                        password.text.toString()
                    )
                }

                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE ->
                            loginViewModel.login(
                                loginEmail.text.toString(),
                                password.text.toString()
                            )
                    }
                    false
                }

                login.setOnClickListener {
                    loginViewModel.login(loginEmail.text.toString(), password.text.toString())
                }
            }
        }
    }

}