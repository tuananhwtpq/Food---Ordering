package com.example.food_order.data.repository

import com.example.food_order.data.api.OrderApiService
import com.example.food_order.data.model.common.Order
import com.example.food_order.data.model.common.PlaceOrderRequest
import com.example.food_order.data.model.common.PlaceOrderResponse
import com.example.food_order.data.model.common.UpdateOrderStatusRequest
import com.example.food_order.data.model.response.OrderResponse
import com.example.food_order.utils.extension.parseError

class OrderRepository(
    private val apiService: OrderApiService
) {

    suspend fun placeOrder(request: PlaceOrderRequest): Result<PlaceOrderResponse> {
        return try {
            val response = apiService.placeOrder(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.parseError() ?: "Place order failed"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOrders(): Result<List<Order>> {
        return try {
            val response = apiService.getOrders()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.orders)
            } else {
                Result.failure(Exception(response.errorBody()?.parseError() ?: "Get orders failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOrderDetails(orderId: String): Result<Order> {
        return try {
            val response = apiService.getOrderDetails(orderId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.parseError() ?: "Get order details failed"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//    suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit> {
//        return try {
//            val request = UpdateOrderStatusRequest(status)
//            val response = apiService.updateOrderStatus(orderId, request)
//            if (response.isSuccessful) {
//                Result.success(Unit)
//            } else {
//                Result.failure(Exception(response.errorBody()?.parseError() ?: "Update order status failed"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
}