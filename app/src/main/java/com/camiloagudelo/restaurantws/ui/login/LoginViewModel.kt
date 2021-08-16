package com.camiloagudelo.restaurantws.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camiloagudelo.restaurantws.R
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.data.auth.AuthRepository
import com.camiloagudelo.restaurantws.domain.LoginRequest
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult =
        MutableStateFlow<Resource<Pair<CurrentUser, LoginRequest>>>(Resource.Empty())
    val loginResult: StateFlow<Resource<Pair<CurrentUser, LoginRequest>>> = _loginResult

    fun login(loginRequest: LoginRequest) {
        if (_loginResult.value is Resource.Loading) {
            return
        }

        viewModelScope.launch {
            authRepository.login(loginRequest)
                .onStart { _loginResult.value = Resource.Loading() }
                .catch { e -> _loginResult.value = Resource.Error(e) }
                .collect { response ->
                    _loginResult.value =
                        Resource.Success(CurrentUser(token = response?.token!!,
                            clientID = response.idCliente,
                            nombre = response.nombre) to loginRequest)
                }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isEmailValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}