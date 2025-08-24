package com.example.food_order.ui.customer.home.detail.categoryDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food_order.data.repository.CategoryRepository
import com.example.food_order.data.repository.RestaurantRepository

class CategoryDetailViewModelFactory(
    private val restaurantRepository: RestaurantRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryDetailViewModel(restaurantRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}