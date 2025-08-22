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
            try {
                val response = authRepository.signup(request)
                if (response.isSuccessful) {
                    response.body()?.let {
                        //sessionManager.saveAuthDetails(it.token, it.role, it.userId)
                        _signupUiState.value = SignupUiState.Success(it)
                    } ?: run {
                        _signupUiState.value = SignupUiState.Error("Response body is null")
                    }
                } else {
                    val errorMsg = "Đăng ký thất bại: Email đã tồn tại (Code ${response.code()})"
                    _signupUiState.value = SignupUiState.Error(errorMsg)
                }
            } catch (e: Exception) {
                _signupUiState.value = SignupUiState.Error(e.message ?: "Đã có lỗi xảy ra")
            }
        }
    }
}