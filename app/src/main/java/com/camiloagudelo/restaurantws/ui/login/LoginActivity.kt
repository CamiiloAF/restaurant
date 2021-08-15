package com.camiloagudelo.restaurantws.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.camiloagudelo.restaurantws.R
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.databinding.ActivityLoginBinding
import com.camiloagudelo.restaurantws.domain.LoginRequest
import com.camiloagudelo.restaurantws.ui.MainActivity
import com.camiloagudelo.restaurantws.ui.auth.BaseAuthActivity
import com.camiloagudelo.restaurantws.ui.sign_up.SignUpActivity
import com.camiloagudelo.restaurantws.utils.afterTextChanged
import com.camiloagudelo.restaurantws.utils.goToActivity
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseAuthActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPreferences()
        binding.txtSignUp.setOnClickListener { goToActivity<SignUpActivity>() }
    }

    private fun checkPreferences() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val loginRequest = LoginRequest.fromJson(
            sharedPref.getString(getString(R.string.saved_remember_user), null)
        )
        if (loginRequest != null) loginViewModel.login(loginRequest)
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
                checkBoxRememberMe.isEnabled = loginState.isDataValid

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
                        saveRememberMePrefs(it.data!!.second)
                        goToHome()
                    }
                }
            }
        }
    }

    private fun goToHome() {
        goToActivity<MainActivity>()
    }

    private fun saveRememberMePrefs(loginRequest: LoginRequest) {
        val sharedPref = this@LoginActivity.getPreferences(Context.MODE_PRIVATE)

        with(sharedPref.edit()) {
            putString(
                getString(R.string.saved_remember_user),
                loginRequest.toJson()
            )
            commit()
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
                                buildLoginRequest()
                            )
                    }
                    false
                }

                login.setOnClickListener {
                    loginViewModel.login(buildLoginRequest())
                }
            }
        }
    }

    private fun buildLoginRequest(): LoginRequest {
        return with(binding) {
            LoginRequest(
                loginEmail.text.toString(),
                password.text.toString()
            )
        }

    }
}