package com.example.food_order.data.model.response

import com.google.gson.annotations.SerializedName


data class MenuResponse(
    val id: String?,
    val restaurantId: String,
    val name: String,
    val description: String?,
    val price: Double,
    val imageUrl: String?,
    val arModelUrl: String?,
    val createdAt: String?
)
// Bọc theo JSON của BE: { "data": [...] } và { "data": { ... } }
data class MenuListResponse(
    @SerializedName(value = "foodItems", alternate = ["data", "items"])
    val foodItems: List<MenuResponse>
)
data class MenuItemResponse(
    val data: MenuResponse
)
