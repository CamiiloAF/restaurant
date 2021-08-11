package com.camiloagudelo.restaurantws.core.di

import com.camiloagudelo.restaurantws.core.api.ApiService
import com.camiloagudelo.restaurantws.data.LoginDataSource
import com.camiloagudelo.restaurantws.data.LoginRepository
import com.camiloagudelo.restaurantws.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { ApiService() }
}

val loginModule = module {
    single { LoginDataSource(get()) }
    single { LoginRepository(get()) }
    viewModel { LoginViewModel(get()) }
}
