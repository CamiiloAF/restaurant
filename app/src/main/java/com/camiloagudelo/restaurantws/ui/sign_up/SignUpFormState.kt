package com.camiloagudelo.restaurantws.ui.sign_up

data class SignUpFormState(
    val nameError: Int? = null,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val cityError: Int? = null,
    val isDataValid: Boolean = false
)