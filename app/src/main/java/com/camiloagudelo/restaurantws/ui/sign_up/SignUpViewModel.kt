package com.camiloagudelo.restaurantws.ui.sign_up

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camiloagudelo.restaurantws.R
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.core.models.ApiResponse
import com.camiloagudelo.restaurantws.data.auth.AuthRepository
import com.camiloagudelo.restaurantws.data.auth.models.SignUpClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SignUpViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    private val _signUpResult = MutableStateFlow<Resource<ApiResponse>>(Resource.Empty())
    val signUpResult: StateFlow<Resource<ApiResponse>> = _signUpResult

    fun signUpClient(signUpClient: SignUpClient) {
        if (signUpResult.value is Resource.Loading) {
            return
        }

        viewModelScope.launch {
            authRepository.signUpClient(signUpClient)
                .onStart { _signUpResult.value = Resource.Loading() }
                .catch { e -> _signUpResult.value = Resource.Error(e) }
                .collect { response ->
                    _signUpResult.value = Resource.Success(response)
                }
        }
    }

    fun loginDataChanged(signUpClient: SignUpClient) {
        var haveError = false

        if (!isEmailValid(signUpClient.correo)) {
            _signUpForm.value = SignUpFormState(emailError = R.string.invalid_email)
            haveError = true
        }
        if (!isPasswordValid(signUpClient.contrasena)) {
            _signUpForm.value = SignUpFormState(passwordError = R.string.invalid_password)
            haveError = true
        }
        if (!isNameValid(signUpClient.nombre)) {
            _signUpForm.value = SignUpFormState(nameError = R.string.invalid_name)
            haveError = true
        }
        if (!isCityValid(signUpClient.ciudad)) {
            _signUpForm.value = SignUpFormState(cityError = R.string.invalid_city)
            haveError = true
        }

        if (!haveError) {
            _signUpForm.value = SignUpFormState(isDataValid = true)

        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isNameValid(name: String): Boolean {
        return name.isNotEmpty()
    }

    private fun isCityValid(city: String): Boolean {
        return city.isNotEmpty()
    }
}