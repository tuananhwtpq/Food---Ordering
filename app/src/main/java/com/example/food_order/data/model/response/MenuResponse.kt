package com.example.food_order.data.model.response


data class MenuResponse(
    val id: String?,
    val restaurantId: String,
    val name: String,
    val description: String?,
    val price: Double?,
    val imageUrl: String?,
    val arModelUrl: String?,
    val createdAt: String?
)
