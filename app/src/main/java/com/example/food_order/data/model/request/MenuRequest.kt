package com.example.food_order.data.model.request


data class MenuRequest(
    val restaurantId: String,
    val name: String,
    val description: String? = null,
    val price: Double,
    val imageUrl: String? = null,
    val arModelUrl: String? = null
)
