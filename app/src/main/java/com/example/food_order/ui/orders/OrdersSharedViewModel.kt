package com.example.food_order.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.repository.IOrdersRepository
import com.example.food_order.data.repository.OrderListSource.OrderItem
import com.example.food_order.data.repository.OrderListSource.OrderSimple
import com.example.food_order.data.repository.OrderListSource.OrderStatus
import kotlinx.coroutines.launch

class OrdersSharedViewModel(
    private val repo: IOrdersRepository
) : ViewModel() {

    private val _pending = MutableLiveData<MutableList<OrderSimple>>(mutableListOf())
    val pending: LiveData<MutableList<OrderSimple>> = _pending

    private val _delivery = MutableLiveData<MutableList<OrderSimple>>(mutableListOf())
    val delivery: LiveData<MutableList<OrderSimple>> = _delivery

    /** Gọi ở onViewCreated của 2 màn để load data thật */
    fun refresh() {
        viewModelScope.launch {
            repo.getOwnerOrders()
                .onSuccess { list ->
                    val a = list.filter { it.status == OrderStatus.PENDING_ACCEPTANCE }
                    val b = list.filter { it.status != OrderStatus.PENDING_ACCEPTANCE }
                    _pending.value = a.toMutableList()
                    _delivery.value = b.toMutableList()
                }
                .onFailure {
                    // Để trống cho nhẹ, UI không bị crash
                }
        }
    }

    fun accept(order: OrderSimple) {
        // Update UI ngay cho mượt; đồng bộ server nền
        val p = _pending.value ?: mutableListOf()
        if (p.remove(order)) {
            val moved = order.copy(status = OrderStatus.ACCEPTED)
            _delivery.value = (_delivery.value ?: mutableListOf()).apply { add(0, moved) }
            _pending.value = p
            viewModelScope.launch { repo.updateStatus(order.id, OrderStatus.ACCEPTED) }
        }
    }

    fun reject(order: OrderSimple) {
        val p = _pending.value ?: mutableListOf()
        if (p.remove(order)) {
            val moved = order.copy(status = OrderStatus.REJECTED)
            _delivery.value = (_delivery.value ?: mutableListOf()).apply { add(0, moved) }
            _pending.value = p
            viewModelScope.launch { repo.updateStatus(order.id, OrderStatus.REJECTED) }
        }
    }
}
