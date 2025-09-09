package com.example.food_order.ui.customer.orders.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.model.common.Order
import com.example.food_order.data.repository.OrderRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val orderRepository: OrderRepository,
    private val orderId: String
) : ViewModel() {

    private val _order = MutableLiveData<Result<Order>>()
    val order: LiveData<Result<Order>> = _order

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var pollingJob: Job? = null

    init {
        startPolling()
    }

    private fun getOrderDetails() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val result = orderRepository.getOrderDetails(orderId)
            _order.postValue(result)
            _isLoading.postValue(false)
        }
    }

    fun startPolling() {
        // Dừng polling hiện tại nếu đang chạy
        stopPolling()

        // Bắt đầu polling
        pollingJob = viewModelScope.launch {
            while (true) {
                getOrderDetails()
                delay(5000)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling() // Dừng polling khi ViewModel bị hủy
    }

}