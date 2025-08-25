package com.example.food_order.utils.state

import com.example.food_order.data.model.common.Restaurant
import com.example.food_order.data.repository.MenuItem

data class RestaurantDetailUiState(
    val isLoading: Boolean = true,
    val restaurant: Restaurant? = null,
    val menuItems: List<MenuItem> = emptyList(),
    val error: String? = null
)