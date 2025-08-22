package com.example.food_order.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.food_order.data.repository.OrderListSource.OrderItem
import com.example.food_order.data.repository.OrderListSource.OrderSimple
import com.example.food_order.data.repository.OrderListSource.OrderStatus

class OrdersSharedViewModel : ViewModel() {

    // Demo list: đơn chờ duyệt ở Home
    private val _pending = MutableLiveData<MutableList<OrderSimple>>(mutableListOf(
        OrderSimple(
            id = "A0999",
            status = OrderStatus.PENDING_ACCEPTANCE,
            customer = "Lê C",
            items = listOf(
                OrderItem(name = "Phở bò", quantity = 2),
                OrderItem(name = "Trà chanh", quantity = 1)
            ),
            address = "23 Láng Hạ, HN",
            note = "Gọi trước khi đến",
            total = 95_000,
            timeText = "08:20",
        ),
        OrderSimple(
            id = "B1234",
            status = OrderStatus.PENDING_ACCEPTANCE,
            customer = "Nguyễn Văn A",
            items = listOf(
                OrderItem(name = "Bún chả", quantity = 2),
                OrderItem(name = "Nem rán", quantity = 1)
            ),
            address = "12 Nguyễn Trãi, Hà Nội",
            note = "Ít cay",
            total = 320_000,
            timeText = "17:20",
        )
    ))
    val pending: MutableLiveData<MutableList<OrderSimple>> get() = _pending

    // Danh sách hiển thị ở màn Delivery
    private val _delivery = MutableLiveData<MutableList<OrderSimple>>(mutableListOf())
    val delivery: MutableLiveData<MutableList<OrderSimple>> get() = _delivery

    fun accept(order: OrderSimple) {
        val list = _pending.value ?: mutableListOf()
        if (list.remove(order)) {
            val moved = order.copy(status = OrderStatus.OUT_FOR_DELIVERY)
            _delivery.value = (_delivery.value ?: mutableListOf()).apply { add(0, moved) }
            _pending.value = list
        }
    }

    fun reject(order: OrderSimple) {
        val list = _pending.value ?: mutableListOf()
        if (list.remove(order)) _pending.value = list
    }
}
