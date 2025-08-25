package com.example.food_order.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.model.request.SignupRequest
import com.example.food_order.data.repository.AuthRepository
import com.example.food_order.manager.SessionManager
import com.example.food_order.utils.state.SignupUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignupViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _signupUiState = MutableStateFlow<SignupUiState>(SignupUiState.Idle)
    val signupUiState: StateFlow<SignupUiState> = _signupUiState

    fun signupUser(request: SignupRequest) {
        viewModelScope.launch {
            _signupUiState.value = SignupUiState.Loading

            val result = authRepository.signup(request)

            result.onSuccess { authResponse ->
                _signupUiState.value = SignupUiState.Success(authResponse)
            }.onFailure { exception ->
                _signupUiState.value = SignupUiState.Error(exception.message ?: "Đã có lỗi xảy ra")
            }
        }
    }
}