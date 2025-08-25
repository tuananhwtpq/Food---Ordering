package com.example.food_order.data.repository

import android.content.Context
import com.example.food_order.data.api.OrdersApiService
import com.example.food_order.data.api.OrdersListResponse
import com.example.food_order.data.model.response.OrderResponse
import com.example.food_order.data.repository.OrderListSource.OrderItem
import com.example.food_order.data.repository.OrderListSource.OrderSimple
import com.example.food_order.data.repository.OrderListSource.OrderStatus
import com.example.food_order.di.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import kotlin.math.roundToLong

interface IOrdersRepository {
    /** Lấy toàn bộ đơn của Owner (theo JWT). FE tự chia tab/khung hiển thị. */
    suspend fun getOwnerOrders(): Result<List<OrderSimple>>
    suspend fun updateStatus(orderId: String, newStatus: OrderStatus): Result<Unit>
}

class OrdersRepository private constructor(
    private val service: OrdersApiService
) : IOrdersRepository {

    companion object {
        fun from(context: Context): OrdersRepository {
            val s = RetrofitInstance.createAuthorizedServiceGeneric<OrdersApiService>(context)
            return OrdersRepository(s)
        }
    }

    override suspend fun getOwnerOrders(): Result<List<OrderSimple>> = safe {
        val resp = service.getOwnerOrders()
        if (!resp.isSuccessful) throw HttpException(resp)
        val body = resp.body() ?: OrdersListResponse(emptyList())
        body.orders.map { it.toUi() }
    }

    override suspend fun updateStatus(orderId: String, newStatus: OrderStatus): Result<Unit> = safe {
        val payload = mapOf("status" to newStatus.name)
        val resp = service.updateOwnerOrderStatus(orderId, payload)
        if (!resp.isSuccessful) throw HttpException(resp)
        Unit
    }

    private suspend inline fun <T> safe(crossinline block: suspend () -> T): Result<T> {
        return try {
            val data = withContext(Dispatchers.IO) { block() }
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/** Map network DTO -> UI model sẵn dùng trong adapter hiện tại (OwnerOrdersAdapter, ListDeliveryAdapter). */
private fun OrderResponse.toUi(): OrderSimple {
    val customerName = "User " + userId.take(8)  // BE chưa có tên khách -> hiển thị gọn
    val prettyAddress = listOfNotNull(address?.addressLine1, address?.city).joinToString(", ")
    val itemsUi = items.map { OrderItem(name = it.menuItemName ?: "Món", quantity = it.quantity) }
    return OrderSimple(
        id = id,
        status = runCatching { OrderStatus.valueOf(status) }.getOrElse { OrderStatus.PENDING_ACCEPTANCE },
        customer = customerName,
        items = itemsUi,
        address = prettyAddress.ifBlank { null },
        note = null,
        total = totalAmount.roundToLong(),
        timeText = "" // BE chưa trả thời gian -> để trống
    )
}
