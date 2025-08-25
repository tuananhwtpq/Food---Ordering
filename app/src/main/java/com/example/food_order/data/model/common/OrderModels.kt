package com.example.food_order.data.model.common

import com.google.gson.annotations.SerializedName

data class PlaceOrderRequest(
    @SerializedName("addressId")
    val addressId: String
)

// Model cho response body của API POST /orders
data class PlaceOrderResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("message")
    val message: String
)

// Model cho response body của API GET /orders
data class GetOrdersResponse(
    @SerializedName("orders")
    val orders: List<Order>
)

// Model cho request body của API PATCH /orders/{id}/status
data class UpdateOrderStatusRequest(
    @SerializedName("status")
    val status: String
)

// Model chính cho một đơn hàng
data class Order(
    @SerializedName("id")
    val id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("restaurantId")
    val restaurantId: String,
    @SerializedName("riderId")
    val riderId: String?,
    @SerializedName("address")
    val address: Address?,
    @SerializedName("status")
    val status: String,
    @SerializedName("paymentStatus")
    val paymentStatus: String,
    @SerializedName("stripePaymentIntentId")
    val stripePaymentIntentId: String?,
    @SerializedName("totalAmount")
    val totalAmount: Double,
    @SerializedName("items")
    val items: List<OrderItem>,
    @SerializedName("restaurant")
    val restaurant: Restaurant?,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

// Model cho một món hàng trong đơn hàng
data class OrderItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("menuItemId")
    val menuItemId: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("menuItemName")
    val menuItemName: String
)