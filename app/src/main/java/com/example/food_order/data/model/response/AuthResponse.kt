package com.example.food_order.data.model.response

data class AuthResponse(
    val userId: String,
    val token: String,
    val role: String,
    val email: String
)