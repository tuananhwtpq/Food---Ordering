package com.example.food_order.data.repository

class OrderListSource {
    data class OrderSimple(
        val id: String,
        val status: OrderStatus,          // "PENDING", "ACCEPTED", ...
        val restaurantId: String?,
        val customer: String?,
        val items: List<OrderItem>,// có thể null
        val address: String?,
        val note: String?,
        val total: Long,             // VND
        val timeText: String,        // "HH:mm" hoặc "dd/MM HH:mm"
    )
    enum class OrderStatus {
        PENDING_ACCEPTANCE, // Initial state when order is placed
        ACCEPTED,          // Restaurant accepted the order
        PREPARING,         // Food is being prepared
        READY,            // Ready for delivery/pickup
        ASSIGNED,
        DELIVERING,
        OUT_FOR_DELIVERY, // Rider picked up
        DELIVERED,        // Order completed
        DELIVERY_FAILED,        // Order completed
        REJECTED,         // Restaurant rejected the order
        CANCELLED         // Customer cancelled
    }
    data class OrderItem(
        val name: String,
        val quantity: Int
    )
}