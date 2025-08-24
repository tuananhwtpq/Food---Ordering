package com.example.food_order.data.model.response

// Bám sát BE model để map nhanh
data class OrderResponse(
    val id: String,
    val userId: String,
    val restaurantId: String,
    val riderId: String?,
    val address: AddressResponse?,
    val status: String,
    val paymentStatus: String?,
    val stripePaymentIntentId: String?,
    val totalAmount: Double,
    val items: List<OrderItemResponse> = emptyList(),
    val restaurant: RestaurantBriefResponse? = null,
    val createdAt: String? = null
)

data class AddressResponse(
    val id: String? = null,
    val userId: String? = null,
    val addressLine1: String,
    val addressLine2: String? = null,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)

data class OrderItemResponse(
    val id: String,
    val orderId: String,
    val menuItemId: String,
    val quantity: Int,
    val menuItemName: String?
)

/** BE có trả kèm restaurant trong một số route owner — để sẵn nếu cần. */
data class RestaurantBriefResponse(
    val id: String,
    val ownerId: String,
    val name: String,
    val address: String?
)
