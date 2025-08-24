package com.example_food_order.data.repository

/**
 * Chỉ giữ model UI dùng cho adapter. Không để fake-data/repository ở đây nữa.
 */
data class MenuItem(
    val id: String? = null,
    val restaurantId: String,
    val name: String,
    val description: String? = null,
    val price: Double,
    val imageUrl: String? = null,
    val arModelUrl: String? = null,
    val createdAt: String? = null
)
