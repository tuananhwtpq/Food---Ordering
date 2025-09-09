package com.example.food_order.ui.customer.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food_order.data.api.RestaurantApiService
import com.example.food_order.data.repository.CategoryRepository
import com.example.food_order.data.repository.RestaurantRepository
import com.example.food_order.manager.SessionManager

class CustomerHomeViewModelFactory(
    private val categoryRepository: CategoryRepository,
    private val restaurantRepository: RestaurantRepository,
    private val restaurantApiService: RestaurantApiService,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomerHomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CustomerHomeViewModel(
                categoryRepository,
                restaurantRepository,
                restaurantApiService,
                sessionManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}