package com.camiloagudelo.restaurantws.core.api

sealed class Resource<T>(val data: T? = null, val error: Throwable? = null) {
    class Empty<T> : Resource<T>()
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(error: Throwable?) : Resource<T>(error = error) {
        init {
            error?.printStackTrace()
        }
    }
}