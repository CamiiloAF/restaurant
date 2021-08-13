package com.camiloagudelo.restaurantws.core.api

import io.ktor.client.statement.*

interface ApiServiceCallback {
    suspend fun execute(): HttpResponse
}