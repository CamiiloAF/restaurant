package com.camiloagudelo.restaurantws.ui.specialty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camiloagudelo.restaurantws.core.api.Resource
import com.camiloagudelo.restaurantws.data.specialty.SpecialityRepository
import com.camiloagudelo.restaurantws.data.specialty.models.Speciality
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SpecialtyViewModel(private val specialityRepository: SpecialityRepository) : ViewModel() {
    private val _specialityResult = MutableStateFlow<Resource<Speciality>>(Resource.Empty())
    val specialityResult: StateFlow<Resource<Speciality>> = _specialityResult

    fun getSpeciality() {
        viewModelScope.launch {
            specialityRepository.getSpeciality()
                .onStart { _specialityResult.value = Resource.Loading() }
                .catch { e -> _specialityResult.value = Resource.Error(e) }
                .collect { response ->
                    _specialityResult.value = Resource.Success(response)
                }
        }
    }
}