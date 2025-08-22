package com.example.food_order.data.model.request


data class SignupRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String
)