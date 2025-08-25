package com.example.food_order.data.model.common

import android.annotation.SuppressLint

@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
import com.example.food_order.data.repository.MenuItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.InternalSerializationApi


@Serializable
data class CartResponse(
    val items: List<CartItem>,
    val checkoutDetails: CheckoutModel
)


@Serializable
data class CheckoutModel(
    val subtotal: Double,
    val deliveryFee: Double,
    val total: Double
)


@Serializable
data class CartItem(
    val id: String,
    val userId: String,
    val restaurantId: String,
    val menuItem: MenuItem,
    val quantity: Int,
    val addedAt: String
)


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AddToCartRequest(
    val restaurantId: String,
    val menuItemId: String,
    val quantity: Int
)


@Serializable
data class UpdateCartItemRequest(
    val cartItemId: String,
    val quantity: Int
)