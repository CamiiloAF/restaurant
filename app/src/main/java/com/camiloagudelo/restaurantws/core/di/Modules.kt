package com.camiloagudelo.restaurantws.core.di

import com.camiloagudelo.restaurantws.core.api.ApiService
import com.camiloagudelo.restaurantws.data.auth.AuthDataSource
import com.camiloagudelo.restaurantws.data.auth.AuthRepository
import com.camiloagudelo.restaurantws.ui.login.LoginViewModel
import com.camiloagudelo.restaurantws.ui.sign_up.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { ApiService() }
}

val authModule = module {
    single { AuthDataSource(get()) }
    single { AuthRepository(get()) }

    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
}
