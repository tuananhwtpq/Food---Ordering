package com.example.food_order.ui.auth.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.model.common.Address
import com.example.food_order.data.repository.AddressRepository
import kotlinx.coroutines.launch

class ProfileSetupViewModel(
    private val addressRepository: AddressRepository
) : ViewModel() {

    private val _saveStatus = MutableLiveData<Result<Unit>>()
    val saveStatus: LiveData<Result<Unit>> = _saveStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveAddress(
        addressLine1: String,
        city: String,
        state: String,
        zipCode: String,
        country: String
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val address = Address(
                id = null,
                userId = null,
                addressLine1 = addressLine1,
                addressLine2 = null,
                city = city,
                state = state,
                zipCode = zipCode,
                country = country,
                latitude = null,
                longitude = null
            )
            val result = addressRepository.addAddress(address)
            _saveStatus.postValue(result)
            _isLoading.postValue(false)
        }
    }
}