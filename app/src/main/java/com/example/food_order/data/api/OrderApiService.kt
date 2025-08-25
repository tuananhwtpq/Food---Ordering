package com.example.food_order.data.api

import com.example.food_order.data.model.common.GetOrdersResponse
import com.example.food_order.data.model.common.Order
import com.example.food_order.data.model.common.PlaceOrderRequest
import com.example.food_order.data.model.common.PlaceOrderResponse
import com.example.food_order.data.model.response.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class OwnerOrdersResponse(
    val orders: List<OrderResponse>
)

interface OrderApiService {


    @POST("orders")
    suspend fun placeOrder(@Body request: PlaceOrderRequest): Response<PlaceOrderResponse>

    @GET("orders")
    suspend fun getOrders(): Response<GetOrdersResponse>

    @GET("orders/{id}")
    suspend fun getOrderDetails(@Path("id") orderId: String): Response<Order>

    @GET("restaurant-owner/orders")
    suspend fun getOwnerOrders(
        @Query("status") status: String? = null
    ): Response<OwnerOrdersResponse>


}