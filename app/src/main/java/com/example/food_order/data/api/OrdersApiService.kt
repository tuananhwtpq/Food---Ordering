package com.example.food_order.data.api

import com.example.food_order.data.model.response.OrderResponse
import retrofit2.Response
import retrofit2.http.*

// BE đang trả {"orders":[...]} ở cả 2 route: owner và user
data class OrdersListResponse(
    val orders: List<OrderResponse>
)


interface OrdersApiService {

    /** Owner: lấy danh sách đơn của chính owner dựa vào JWT (có thể kèm status). */
    @GET("restaurant-owner/orders")
    suspend fun getOwnerOrders(
        @Query("status") status: String? = null
    ): Response<OrdersListResponse>

    /** Customer (nếu cần dùng sau này). */
    @GET("orders/user")
    suspend fun getMyOrders(): Response<OrdersListResponse>

    /** Owner cập nhật trạng thái đơn. BE: PATCH /restaurant-owner/orders/{orderId}/status */
    @PATCH("restaurant-owner/orders/{orderId}/status")
    suspend fun updateOwnerOrderStatus(
        @Path("orderId") orderId: String,
        @Body body: Map<String, String> // { "status": "ACCEPTED" }
    ): Response<SimpleMessage>
}
