package com.example.food_order.utils.state


import com.example.food_order.data.model.common.Category
import com.example.food_order.data.model.common.Restaurant
import com.example.food_order.data.repository.MenuItem

data class CustomerHomeUiState(
    val isLoading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val restaurants: List<Restaurant> = emptyList(),
    val popularItems: List<MenuItem> = emptyList(),
    val error: String? = null
)