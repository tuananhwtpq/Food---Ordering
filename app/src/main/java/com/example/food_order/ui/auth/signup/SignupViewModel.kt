package com.example.food_order.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.model.request.SignupRequest
import com.example.food_order.data.repository.AuthRepository
import com.example.food_order.manager.SessionManager
import com.example.food_order.utils.extension.isValidEmail
import com.example.food_order.utils.state.SignupUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignupViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _signupUiState = MutableStateFlow<SignupUiState>(SignupUiState.Idle)
    val signupUiState: StateFlow<SignupUiState> = _signupUiState

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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

    fun validateLoginInput(
        username: String?,
        email: String?,
        password: String?,
        reEnterPw: String?,
        checkbox: Boolean?
    ): Boolean {
        return when {
            username.isNullOrBlank() -> {
                _error.update { "Username không được để trống" }
                false
            }

            email.isNullOrBlank() -> {
                _error.update { "Email không được để trống" }
                false
            }

            !email.isValidEmail() -> {
                _error.update { "Định dạng email không chính xác!" }
                false
            }

            password.isNullOrBlank() -> {
                _error.update { "Password không được để trống" }
                false
            }

            password.length < 6 -> {
                _error.update { "Password phải có ít nhất 6 ký tự" }
                false
            }

            !Regex("[^a-zA-Z0-9]").containsMatchIn(password) -> {
                _error.update { "Mật khẩu phải chứa ít nhất 1 ký tự đặc biệt!" }
                false
            }

            reEnterPw.isNullOrBlank() -> {
                _error.update { "Vui lòng nhập đầy đủ thông tin!" }
                false
            }

            reEnterPw != password -> {
                _error.update { "Mật khẩu không khớp!" }
                false
            }

            checkbox == false -> {
                _error.update { "Bạn cần đồng ý với các điều khoản của chúng tôi!" }
                false
            }

            else -> {
                clearError()
                true
            }
        }
    }

    fun clearError() {
        _error.update { null }
    }
}