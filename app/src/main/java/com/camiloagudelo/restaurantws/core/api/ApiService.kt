package com.camiloagudelo.restaurantws.core.api

//import com.camiloagudelo.restaurantws.core.models.ApiResponse
//import com.camiloagudelo.restaurantws.data.auth.models.LoggedInUser
//import com.camiloagudelo.restaurantws.data.auth.models.SignUpClient
//import retrofit2.Call
//import retrofit2.http.Body
//import retrofit2.http.GET
//import retrofit2.http.POST
//import retrofit2.http.Query

//interface ApiService {
//    @GET("api/clientes")
//    suspend fun login(
//        @Query("correo") email: String,
//        @Query("contrasena") password: String
//    ): Call<LoggedInUser>
//
//    @POST("api/clientes")
//    fun signUpClient(@Body signUpClient: SignUpClient): Call<ApiResponse>
//}


import com.camiloagudelo.restaurantws.core.models.ApiResponse
import com.camiloagudelo.restaurantws.data.auth.models.CurrentUser
import com.camiloagudelo.restaurantws.data.auth.models.LoggedInUser
import com.camiloagudelo.restaurantws.data.auth.models.PrivacyPoliciesResponse
import com.camiloagudelo.restaurantws.data.auth.models.SignUpClient
import com.camiloagudelo.restaurantws.data.home.models.CategoriesResponse
import com.camiloagudelo.restaurantws.data.pedidos.models.Pedido
import com.camiloagudelo.restaurantws.data.pedidos.models.PedidosResponse
import com.camiloagudelo.restaurantws.data.products.models.ProductsResponse
import com.camiloagudelo.restaurantws.data.specialty.models.SpecialityResponse
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ApiService {

    private val client = HttpClient(Android) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(JsonFeature) {
            serializer = GsonSerializer()
        }

        defaultRequest {
            host = "wsc.fabricasoftware.co/api"
            url {
                protocol = URLProtocol.HTTPS
                header(HttpHeaders.Accept, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }

    private suspend inline fun <reified T> doCall(apiServiceCallback: ApiServiceCallback): T {
        try {
            val response = apiServiceCallback.execute()
            val json = String(response.readBytes())
            val apiResponse = Gson().fromJson(json, ApiResponse::class.java)

            if (apiResponse.respuesta != "OK") {
                throw Exception(apiResponse.mensaje)
            }

            return Gson().fromJson(json, T::class.java)
        } catch (e: ClientRequestException) {
            throw Exception(e.response.status.description)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun login(email: String, password: String): LoggedInUser {
        val callback = object : ApiServiceCallback {
            override suspend fun execute(): HttpResponse {
                return client.get(path = "clientes") {
                    url {
                        parameter("contrasena", password)
                        parameter("correo", email)
                    }
                }
            }

        }
        return doCall(callback)
    }

    suspend fun getPrivacyPolicies(): PrivacyPoliciesResponse {
        val callback = object : ApiServiceCallback {
            override suspend fun execute(): HttpResponse {
                return client.get(path = "politicas?ver")
            }
        }
        return doCall(callback)
    }

    suspend fun signUpClient(signUpClient: SignUpClient): ApiResponse {
        val callback = object : ApiServiceCallback {
            override suspend fun execute(): HttpResponse {
                return client.post(path = "clientes") {
                    body = signUpClient
                }
            }
        }
        return doCall(callback)
    }

    suspend fun getCategories(): CategoriesResponse {
        val callback = object : ApiServiceCallback {
            override suspend fun execute(): HttpResponse = client.get(path = "categorias")
        }
        return doCall(callback)
    }

    suspend fun getProductsByCategory(categoryId: Int): ProductsResponse {
        val callback = object : ApiServiceCallback {
            override suspend fun execute(): HttpResponse =
                client.get(path = "categorias/$categoryId")
        }
        return doCall(callback)
    }

    suspend fun getPedidosByClient(currentUser: CurrentUser): PedidosResponse {
        val callback = object : ApiServiceCallback {
            override suspend fun execute(): HttpResponse =
                client.get(path = "pedidos") {
                    parameter("cliente", currentUser.idCliente)
                    parameter("token", currentUser.token)
                }
        }
        return doCall(callback)
    }

    suspend fun getSpeciality(): SpecialityResponse {
        val callback = object : ApiServiceCallback {
            override suspend fun execute(): HttpResponse {
                return client.get(path = "especialidad")
            }
        }
        return doCall(callback)
    }

    suspend fun sendPedido(pedido: Pedido): ApiResponse{
        val callback = object : ApiServiceCallback {
            override suspend fun execute(): HttpResponse {
                return client.post(path = "pedidos"){
                    body = pedido
                }
            }
        }
        return doCall(callback)
    }

}