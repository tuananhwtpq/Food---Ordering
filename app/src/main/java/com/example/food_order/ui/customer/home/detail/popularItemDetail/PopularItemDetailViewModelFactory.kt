package com.example.food_order.ui.customer.home.detail.popularItemDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food_order.data.repository.CartRepository
import com.example.food_order.data.repository.RestaurantRepository

class PopularItemDetailViewModelFactory(
    private val restaurantRepository: RestaurantRepository,
    private val cartRepository: CartRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PopularItemDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PopularItemDetailViewModel(restaurantRepository, cartRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}