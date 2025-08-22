package com.example.food_order.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.model.request.LoginRequest
import com.example.food_order.data.repository.AuthRepository
import com.example.food_order.manager.SessionManager
import com.example.food_order.utils.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun loginUser(request: LoginRequest) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading
            try {
                val response = authRepository.login(request)
                if (response.isSuccessful) {
                    response.body()?.let {
                        // ViewModel vẫn chịu trách nhiệm lưu token
                        sessionManager.saveAuthDetails(it.token, it.role)
                        _loginUiState.value = LoginUiState.Success(it)
                    } ?: run {
                        _loginUiState.value = LoginUiState.Error("Response body is null")
                    }
                } else {
                    val errorMsg = "Login failed: ${response.code()}"
                    _loginUiState.value = LoginUiState.Error(errorMsg)
                }
            } catch (e: Exception) {
                _loginUiState.value = LoginUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}