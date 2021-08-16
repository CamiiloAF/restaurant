package com.camiloagudelo.restaurantws.ui.sign_up

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.data.auth.models.SignUpClient
import com.camiloagudelo.restaurantws.databinding.ActivitySignUpBinding
import com.camiloagudelo.restaurantws.domain.LoginRequest
import com.camiloagudelo.restaurantws.ui.MainActivity
import com.camiloagudelo.restaurantws.ui.auth.BaseAuthActivity
import com.camiloagudelo.restaurantws.ui.login.LoginViewModel
import com.camiloagudelo.restaurantws.ui.specialty.SpecialtyActivity
import com.camiloagudelo.restaurantws.utils.afterTextChanged
import com.camiloagudelo.restaurantws.utils.goToActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : BaseAuthActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val signUpViewModel: SignUpViewModel by viewModel()
    private val loginViewModel: LoginViewModel by viewModel()

    override fun initializeBinding() {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOnClickListeners()
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
                        login()
                    }
                }
            }
        }

    }

    private fun login() {
        val signUpClient = buildSignUpClient()

        with(loginViewModel, {
            login(
                with(signUpClient) { LoginRequest(correo, contrasena) }
            )
        })

        observeLoginAction()
    }

    private fun observeLoginAction() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginResult.collect {
                when (it) {
                    is Resource.Empty -> {
                    }
                    is Resource.Error -> {
                        showActionFailed(it.error?.message ?: "Error inesperado")
                        binding.signUpLoading.visibility = View.GONE
                    }
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        binding.signUpLoading.visibility = View.GONE
                        binding.btnSignUp.visibility = View.VISIBLE
                        goToSpecialityActivity()
                    }
                }
            }
        }
    }

    private fun goToSpecialityActivity() {
        goToActivity<SpecialtyActivity>()
        finish()
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
            }
        }
    }

    private fun setOnClickListeners() {
        binding.btnSignUp.setOnClickListener {
            showPrivacyPoliciesDialog()
        }
    }

    private fun showPrivacyPoliciesDialog() {
        MaterialAlertDialogBuilder(this).apply {
            title = "Políticas de privacidad"
            setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur luctus mi lorem, at aliquam risus fermentum eu. Cras at nisl vel lorem suscipit vulputate. Curabitur nec augue fermentum, lacinia est a, euismod enim. Proin tortor tellus, posuere quis finibus non, rhoncus nec augue. Pellentesque in ipsum nec risus dictum tristique et et ligula. Pellentesque aliquam lectus nec suscipit aliquam. Donec volutpat est venenatis, fermentum justo in, aliquam ante. Nullam at sodales orci. Donec ultricies placerat purus, eu eleifend mauris porttitor nec. Cras eu suscipit felis, eu consequat odio. Etiam pharetra orci vitae est eleifend, nec semper magna laoreet. Sed placerat, tortor sed egestas sagittis, est sapien lobortis ipsum, fringilla vulputate lacus libero a dui. In sit amet efficitur libero. In vel nisl hendrerit, sodales erat et, tempus lacus. Vestibulum eleifend non libero id vehicula.")
            setPositiveButton("Aceptar") { _, _ ->
                signUpViewModel.signUpClient(buildSignUpClient())
            }
            setNegativeButton(android.R.string.cancel) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            show()
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