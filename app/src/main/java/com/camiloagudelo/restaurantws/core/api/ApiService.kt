package com.camiloagudelo.restaurantws.core.api

import com.camiloagudelo.restaurantws.core.models.ApiResponse
import com.camiloagudelo.restaurantws.data.model.LoggedInUser
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
            level = LogLevel.HEADERS
        }

        install(JsonFeature) {
            serializer = GsonSerializer()
        }

        defaultRequest {
            host = "wsc.fabricasoftware.co/api"
            url {
                protocol = URLProtocol.HTTPS
            }
        }

        HttpResponseValidator {
            var x = false
            validateResponse { response ->
                if (response.status == HttpStatusCode.OK && !x) {
                    val json = String(response.readBytes())
                    val apiResponse = Gson().fromJson(json, ApiResponse::class.java)

                    if (apiResponse.respuesta == "ERROR") {
                        x = true
                        throw Exception(apiResponse.mensaje)
                    }
                }
            }
            handleResponseException { exception ->
                if (exception !is ClientRequestException) return@handleResponseException
                val exceptionResponse = exception.response
                if (exceptionResponse.status == HttpStatusCode.NotFound) {
                    val exceptionResponseText = exceptionResponse.readText()
                    print(exceptionResponseText)
                }
            }


        }

    }

    suspend fun login(email: String, password: String): LoggedInUser {
        return client.get(path = "clientes") {
            url {
                parameter("contrasena", password)
                parameter("correo", email)
            }
        }
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