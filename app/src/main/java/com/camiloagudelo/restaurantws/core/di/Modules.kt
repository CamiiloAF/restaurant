package com.camiloagudelo.restaurantws.core.di

import com.camiloagudelo.restaurantws.core.api.ApiService
import com.camiloagudelo.restaurantws.core.database.RestaurantWSDatabase
import com.camiloagudelo.restaurantws.data.auth.AuthRepository
import com.camiloagudelo.restaurantws.data.home.repositories.HomeRepository
import com.camiloagudelo.restaurantws.data.pedidos.PedidosRepository
import com.camiloagudelo.restaurantws.data.products.ProductRepository
import com.camiloagudelo.restaurantws.data.specialty.SpecialityRepository
import com.camiloagudelo.restaurantws.ui.MainViewModel
import com.camiloagudelo.restaurantws.ui.category_detail.CategoryDetailViewModel
import com.camiloagudelo.restaurantws.ui.home.HomeViewModel
import com.camiloagudelo.restaurantws.ui.login.LoginViewModel
import com.camiloagudelo.restaurantws.ui.pedido_detail.PedidoDetailViewModel
import com.camiloagudelo.restaurantws.ui.pedidos.PedidosViewModel
import com.camiloagudelo.restaurantws.ui.sign_up.SignUpViewModel
import com.camiloagudelo.restaurantws.ui.specialty.SpecialtyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val coreModule = module {
    single { ApiService() }
    single { RestaurantWSDatabase.getDatabase(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val authModule = module {
    single { AuthRepository(get()) }

    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
}

val specialityModule = module {
    single { SpecialityRepository(get()) }

    viewModel { SpecialtyViewModel(get()) }
}

val homeModule = module {
    single { HomeRepository(get()) }

    viewModel { HomeViewModel(get()) }
}

val categoryDetailModule = module {
    single { ProductRepository(get(),get<RestaurantWSDatabase>().pedidosDao()) }

    viewModel { CategoryDetailViewModel(get()) }
}

val pedidosModule = module {
    single { PedidosRepository(get(), get<RestaurantWSDatabase>().pedidosDao()) }

    viewModel { PedidosViewModel(get()) }
}

val pedidosDetailProductModule = module {
    viewModel { PedidoDetailViewModel(get()) }
}
