package com.example.food_order.ui.customer.orders

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.model.common.Order
import com.example.food_order.data.repository.OrderRepository
import kotlinx.coroutines.delay
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

    fun refreshOrderHistory() {
        viewModelScope.launch {
            Log.d("OrderHistoryViewModel", "Refreshing order history with delay")
            _isLoading.postValue(true)
            delay(1000)
            val result = orderRepository.getOrders()
            _orders.postValue(result)
            _isLoading.postValue(false)
        }
    }

    fun refreshData() {
        getOrderHistory()

    }
}