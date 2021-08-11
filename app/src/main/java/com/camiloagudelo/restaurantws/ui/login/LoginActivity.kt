package com.camiloagudelo.restaurantws.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.databinding.ActivityLoginBinding
import com.camiloagudelo.restaurantws.utils.afterTextChanged
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeLoginFromState()
        observeLogin()
        setUpInputsValidations()
    }

    private fun observeLoginFromState() {
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

    private fun observeLogin() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginResult.collect {
                when (it) {
                    is Resource.Empty -> {

                    }
                    is Resource.Error -> {
                        showLoginFailed(it.error?.message ?: "Error inesperado")
                        binding.loading.visibility = View.GONE
                    }
                    is Resource.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.loading.visibility = View.GONE
                        showLoginFailed(it.data!!.toString())
                    }
                }
            }
        }
    }

    private fun setUpInputsValidations() {
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

    private fun showLoginFailed(errorString: String) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}