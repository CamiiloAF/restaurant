package com.camiloagudelo.restaurantws.data.specialty

import com.camiloagudelo.restaurantws.core.api.ApiService
import com.camiloagudelo.restaurantws.data.specialty.models.Speciality
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SpecialityRepository(private val apiService: ApiService) {
    suspend fun getSpeciality(): Flow<Speciality> = flow {
        emit(apiService.getSpeciality().speciality)
    }.flowOn(Dispatchers.IO)
}