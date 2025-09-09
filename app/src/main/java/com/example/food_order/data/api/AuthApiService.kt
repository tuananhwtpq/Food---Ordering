package com.example.food_order.data.api

import com.example.food_order.data.model.request.LoginRequest
import com.example.food_order.data.model.request.SignupRequest
import com.example.food_order.data.model.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>


}