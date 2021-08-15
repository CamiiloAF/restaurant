package com.camiloagudelo.restaurantws.core.di

import com.camiloagudelo.restaurantws.core.api.ApiService
import com.camiloagudelo.restaurantws.data.auth.AuthRepository
import com.camiloagudelo.restaurantws.data.products.ProductRepository
import com.camiloagudelo.restaurantws.data.home.repositories.HomeRepository
import com.camiloagudelo.restaurantws.ui.category_detail.CategoryDetailViewModel
import com.camiloagudelo.restaurantws.ui.home.HomeViewModel
import com.camiloagudelo.restaurantws.ui.login.LoginViewModel
import com.camiloagudelo.restaurantws.ui.sign_up.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { ApiService() }
}

val authModule = module {
    single { AuthRepository(get()) }

    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
}

val homeModule = module {
    single { HomeRepository(get()) }

    viewModel { HomeViewModel(get()) }
}

val categoryDetailModule = module {
    single { ProductRepository(get()) }

    viewModel { CategoryDetailViewModel(get()) }
}
