package com.example.food_order.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.model.request.LoginRequest
import com.example.food_order.data.repository.AuthRepository
import com.example.food_order.manager.SessionManager
import com.example.food_order.utils.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    fun loginUser(request: LoginRequest) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading

            authRepository.login(request)
                .onSuccess { authResponse ->
                    _loginUiState.value = LoginUiState.Success(authResponse)
                }
                .onFailure { exception ->
                    _loginUiState.value =
                        LoginUiState.Error(exception.message ?: "An unknown error occurred")
                }
        }
    }
}