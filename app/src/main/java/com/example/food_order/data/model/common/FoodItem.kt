package com.example.food_order.data.model.common

data class FoodItem(
    val id: String,
    val restaurantId: String,
    val name: String,
    val description: String?,
    val price: Double,
    val imageUrl: String?,
    val arModelUrl: String?,
    val category: String? = null,
    val isAvailable: Boolean = true,
    val createdAt: String
)