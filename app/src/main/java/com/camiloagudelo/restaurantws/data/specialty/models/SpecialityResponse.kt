package com.camiloagudelo.restaurantws.data.specialty.models

import com.google.gson.annotations.SerializedName

data class SpecialityResponse(
    @SerializedName("datos")
    val speciality: Speciality,
    val respuesta: String
)