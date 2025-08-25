package com.example.food_order.ui.customer.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.model.common.Order
import com.example.food_order.data.repository.OrderRepository
import kotlinx.coroutines.launch

class OrderHistoryViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _orders = MutableLiveData<Result<List<Order>>>()
    val orders: LiveData<Result<List<Order>>> = _orders

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getOrderHistory()
    }

    fun getOrderHistory() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val result = orderRepository.getOrders()
            _orders.postValue(result)
            _isLoading.postValue(false)
        }
    }
}