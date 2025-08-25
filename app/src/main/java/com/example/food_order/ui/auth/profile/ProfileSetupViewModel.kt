package com.example.food_order.ui.auth.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.model.common.Address
import com.example.food_order.data.model.common.AddressResponse
import com.example.food_order.data.repository.AddressRepository
import com.example.food_order.manager.SessionManager
import kotlinx.coroutines.launch

class ProfileSetupViewModel(
    private val addressRepository: AddressRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _saveStatus = MutableLiveData<Result<AddressResponse>>()
    val saveStatus: LiveData<Result<AddressResponse>> = _saveStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveAddress(
        addressLine1: String,
        city: String,
        state: String,
        zipCode: String,
        country: String,
        latitude: Double?,
        longitude: Double?
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val address = Address(
                id = null,
                userId = sessionManager.fetchUserId(),
                addressLine1 = addressLine1,
                addressLine2 = null,
                city = city,
                state = state,
                zipCode = zipCode,
                country = country,
                latitude = latitude,
                longitude = longitude
            )
            val result = addressRepository.addAddress(address)
            _saveStatus.postValue(result)

            result.onSuccess { response ->
                sessionManager.saveAddressId(response.id)
                android.util.Log.d("ProfileSetupViewModel", "Đã lưu addressId: ${response.id}")
            }.onFailure { e ->
                android.util.Log.e("ProfileSetupViewModel", "Lỗi thêm địa chỉ: ${e.message}")
            }
            _isLoading.postValue(false)
        }
    }
}