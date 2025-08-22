package com.example.food_order.data.model.request


data class LoginRequest(
    val email: String,
    val password: String,
    val role: String
)
