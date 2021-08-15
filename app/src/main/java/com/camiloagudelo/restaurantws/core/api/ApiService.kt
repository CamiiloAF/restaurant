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
import com.camiloagudelo.restaurantws.data.auth.models.LoggedInUser
import com.camiloagudelo.restaurantws.data.auth.models.SignUpClient
import com.camiloagudelo.restaurantws.data.home.models.CategoriesResponse
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

            if (apiResponse.respuesta == "ERROR") {
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

//    suspend fun saveAuto(autosModel: AutosModel): AutosModel {
//        client.post<HttpResponse>("https://crudcrud.com/api/dd89d34f868344cf8140f5a8e166fc3b/autos") {
//            body = autosModel
//            header(HttpHeaders.Accept, ContentType.Application.Json)
//            contentType(ContentType.Application.Json)
//        }
//
//        return autosModel
//
//    }

//    suspend fun updateAuto(autosModel: AutosModel): AutosModel {
//        client.put<HttpResponse>("https://crudcrud.com/api/dd89d34f868344cf8140f5a8e166fc3b/autos/${autosModel.id}") {
//            body = autosModel
//            header(HttpHeaders.Accept, ContentType.Application.Json)
////            header(HttpHeaders.Allow, AlloM)
//            contentType(ContentType.Application.Json)
//        }
//        return autosModel
//    }

//    suspend fun deleteAuto(autoId: String): String {
//        client.delete<HttpResponse>("https://crudcrud.com/api/dd89d34f868344cf8140f5a8e166fc3b/autos/$autoId")
//        return autoId
//    }
}