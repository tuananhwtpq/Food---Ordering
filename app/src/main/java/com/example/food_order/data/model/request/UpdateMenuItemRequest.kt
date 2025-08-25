package com.example.food_order.data.model.request

data class UpdateMenuItemRequest(
    val name: String? = null,
    val description: String? = null,
    val price: Double? = null,
    val imageUrl: String? = null,
    val category: String? = null,
    val isAvailable: Boolean? = null
)
