package com.camiloagudelo.restaurantws.data.auth.models

import com.google.gson.annotations.SerializedName

data class PrivacyPoliciesResponse(
    @SerializedName("datos")
    val policies: Policies,
    val respuesta: String
)