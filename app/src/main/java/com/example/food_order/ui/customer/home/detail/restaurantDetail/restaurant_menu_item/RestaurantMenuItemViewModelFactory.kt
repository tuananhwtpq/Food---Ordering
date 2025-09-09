package com.example.food_order.ui.customer.home.detail.restaurantDetail.restaurant_menu_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food_order.data.repository.CartRepository
import com.example.food_order.data.repository.RestaurantRepository

class RestaurantMenuItemViewModelFactory(
    private val repository: RestaurantRepository,
    private val cartRepository: CartRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RestaurantMenuItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RestaurantMenuItemViewModel(repository, cartRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}