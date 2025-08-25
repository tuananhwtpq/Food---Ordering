package com.example.food_order.ui.auth.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food_order.data.repository.AddressRepository
import com.example.food_order.manager.SessionManager

class ProfileSetupViewModelFactory(
    private val addressRepository: AddressRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileSetupViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileSetupViewModel(addressRepository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}