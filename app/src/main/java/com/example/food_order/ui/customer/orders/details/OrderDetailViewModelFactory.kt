package com.example.food_order.ui.customer.orders.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food_order.data.repository.OrderRepository

class OrderDetailViewModelFactory(
    private val orderRepository: OrderRepository,
    private val orderId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderDetailViewModel::class.java)) {
            return OrderDetailViewModel(orderRepository, orderId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}