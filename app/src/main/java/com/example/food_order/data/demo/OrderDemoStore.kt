package com.example.food_order.data.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.food_order.data.repository.OrderListSource.OrderItem
import com.example.food_order.data.repository.OrderListSource.OrderSimple
import com.example.food_order.data.repository.OrderListSource.OrderStatus

/**
 * Store demo chia sẻ giữa OwnerHomeFragment & DeliveryFragment.
 * Chỉ thay đổi trong demo; sau này thay bằng API/Repository thật rất dễ.
 */
object OrderDemoStore {

    private val _orders = MutableLiveData<MutableList<OrderSimple>>(mutableListOf(
        OrderSimple(
            id = "A1001",
            status = OrderStatus.PENDING_ACCEPTANCE,
            customer = "Nguyễn Văn A",
            items = listOf(OrderItem("Phở bò", 2), OrderItem("Trà chanh", 1)),
            address = "12 Nguyễn Trãi, Hà Nội",
            note = "Ít cay",
            total = 120_000,
            timeText = "08:40"
        ),
        OrderSimple(
            id = "A1002",
            status = OrderStatus.PENDING_ACCEPTANCE,
            customer = "Trần Thị B",
            items = listOf(OrderItem("Bún chả", 1)),
            address = "56 Hai Bà Trưng, HN",
            note = null,
            total = 60_000,
            timeText = "09:10"
        ),
        OrderSimple(
            id = "A0999",
            status = OrderStatus.OUT_FOR_DELIVERY,
            customer = "Lê C",
            items = listOf(OrderItem("Cơm gà", 1), OrderItem("Coca", 1)),
            address = "23 Láng Hạ, HN",
            note = "Gọi trước khi đến",
            total = 95_000,
            timeText = "08:20"
        ),
        OrderSimple(
            id = "A0998",
            status = OrderStatus.DELIVERED,
            customer = "Phạm D",
            items = listOf(OrderItem("Bánh mì", 3)),
            address = "1 Kim Mã, HN",
            note = "",
            total = 75_000,
            timeText = "Hôm qua"
        )
    ))

    val orders: LiveData<MutableList<OrderSimple>> = _orders

    fun accept(orderId: String) = updateStatus(orderId, OrderStatus.ACCEPTED)
    fun reject(orderId: String) = updateStatus(orderId, OrderStatus.REJECTED)

    private fun updateStatus(orderId: String, newStatus: OrderStatus) {
        val cur = _orders.value ?: return
        val updated = cur.map {
            if (it.id == orderId) it.copy(status = newStatus) else it
        }.toMutableList()
        _orders.value = updated
    }
}
