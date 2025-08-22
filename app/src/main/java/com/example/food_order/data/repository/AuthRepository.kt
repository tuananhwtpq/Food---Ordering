package com.example.food_order.data.repository

import com.example.food_order.data.model.request.LoginRequest
import com.example.food_order.data.model.request.SignupRequest
import com.example.food_order.di.RetrofitInstance

class AuthRepository {

    private val authApiService = RetrofitInstance.authApi

    suspend fun signup(request: SignupRequest) = authApiService.signup(request)

    suspend fun login(request: LoginRequest) = authApiService.login(request)

}