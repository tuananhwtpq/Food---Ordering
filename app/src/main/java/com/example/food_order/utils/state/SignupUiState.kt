package com.example.food_order.utils.state

import com.example.food_order.data.model.response.AuthResponse

sealed class SignupUiState {
    object Idle : SignupUiState()
    object Loading : SignupUiState()
    data class Success(val authResponse: AuthResponse) : SignupUiState()
    data class Error(val message: String) : SignupUiState()
}