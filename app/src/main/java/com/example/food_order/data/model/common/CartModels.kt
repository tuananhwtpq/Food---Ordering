package com.example.food_order.data.model.common

import com.google.gson.annotations.SerializedName
import com.example.food_order.data.repository.MenuItem


data class CartResponse(
    @SerializedName("items")
    val items: List<CartItem>,
    @SerializedName("checkoutDetails")
    val checkoutDetails: CheckoutModel
)

data class CheckoutModel(
    @SerializedName("subTotal")
    val subtotal: Double,
    @SerializedName("deliveryFee")
    val deliveryFee: Double,
    @SerializedName("totalAmount")
    val total: Double
)

data class CartItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("restaurantId")
    val restaurantId: String,
    @SerializedName("menuItemId")
    val menuItem: MenuItem,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("addedAt")
    val addedAt: String
)

data class AddToCartRequest(
    @SerializedName("restaurantId")
    val restaurantId: String,
    @SerializedName("menuItemId")
    val menuItemId: String,
    @SerializedName("quantity")
    val quantity: Int
)

data class UpdateCartItemRequest(
    @SerializedName("cartItemId")
    val cartItemId: String,
    @SerializedName("quantity")
    val quantity: Int
)


