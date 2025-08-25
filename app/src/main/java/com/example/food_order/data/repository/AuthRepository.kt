package com.example.food_order.data.repository

import com.example.food_order.data.api.AuthApiService
import com.example.food_order.data.model.request.LoginRequest
import com.example.food_order.data.model.request.SignupRequest
import com.example.food_order.data.model.response.AuthResponse
import com.example.food_order.manager.SessionManager
import com.example.food_order.utils.extension.parseError

class AuthRepository(
    private val authApiService: AuthApiService,
    private val sessionManager: SessionManager
) {

    suspend fun signup(request: SignupRequest): Result<AuthResponse> {
        return try {
            val response = authApiService.signup(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = response.errorBody()?.parseError()
                Result.failure(Exception(errorMessage ?: "Signup failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response = authApiService.login(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    sessionManager.saveAuthDetails(body)
                    Result.success(body)
                } else {
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                val errorMessage = response.errorBody()?.parseError()
                Result.failure(
                    Exception(
                        errorMessage ?: "Login failed with code: ${response.code()}"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}