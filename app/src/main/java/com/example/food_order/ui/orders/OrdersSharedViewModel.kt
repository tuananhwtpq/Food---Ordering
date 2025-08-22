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
        ),
        OrderSimple(
            id = "B1235",
            status = OrderStatus.PENDING_ACCEPTANCE,
            customer = "Nguyễn Văn B",
            items = listOf(
                OrderItem(name = "Bún chả", quantity = 2),
                OrderItem(name = "Nem rán", quantity = 14)
            ),
            address = "12 Nguyễn Trãi, Hà Nội",
            note = "Ít cay",
            total = 550_000,
            timeText = "17:20",
        ),
        OrderSimple(
            id = "B1236",
            status = OrderStatus.PENDING_ACCEPTANCE,
            customer = "Nguyễn Văn C",
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
    val pending: LiveData<MutableList<OrderSimple>> get() = _pending

    // Danh sách hiển thị ở màn Delivery
    private val _delivery = MutableLiveData<MutableList<OrderSimple>>(mutableListOf(
        OrderSimple(
            id = "A9312",
            status = OrderStatus.DELIVERY_FAILED,
            customer = "Đào Duy",
            items = listOf(
                OrderItem(name = "Phở gà lá é", quantity = 1),
                OrderItem(name = "Trà chanh", quantity = 1)
            ),
            address = "112 Nguyên Xá, HN",
            note = "Nhiều cay",
            total = 95_000,
            timeText = "08:20",
        ),
        OrderSimple(
            id = "C11223",
            status = OrderStatus.DELIVERY_FAILED,
            customer = "Trần Tuấn Anh",
            items = listOf(
                OrderItem(name = "Nem Nướng", quantity = 2),
                OrderItem(name = "Nem rán", quantity = 1)
            ),
            address = "12 Nguyễn Trãi, Hà Nội",
            note = "Ít cay, nhiều rau",
            total = 120_000,
            timeText = "13:10",
        ),
        OrderSimple(
            id = "E1235",
            status = OrderStatus.DELIVERED,
            customer = "Trần Danh Khang",
            items = listOf(
                OrderItem(name = "Bún bò Huế", quantity = 1),
                OrderItem(name = "Cơm rang thập cẩm", quantity = 1)
            ),
            address = "12 Nguyễn Trãi, Hà Nội",
            note = "Nhiều cơm ít bún",
            total = 100_000,
            timeText = "13:20",
        ),
        OrderSimple(
            id = "F1236",
            status = OrderStatus.DELIVERED,
            customer = "Trịnh Ngọc Hải",
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
    val delivery: MutableLiveData<MutableList<OrderSimple>> get() = _delivery

    fun accept(order: OrderSimple) {
        val list = _pending.value ?: mutableListOf()
        if (list.remove(order)) {
            val moved = order.copy(status = OrderStatus.ACCEPTED)
            _delivery.value = (_delivery.value ?: mutableListOf()).apply { add(0, moved) }
            _pending.value = list
        }
    }

    fun reject(order: OrderSimple) {
        val list = _pending.value ?: mutableListOf()
        if (list.remove(order)) {
            val moved = order.copy(status = OrderStatus.REJECTED)
            _delivery.value = (_delivery.value ?: mutableListOf()).apply { add(0, moved) }
            _pending.value = list
        }
    }
}
