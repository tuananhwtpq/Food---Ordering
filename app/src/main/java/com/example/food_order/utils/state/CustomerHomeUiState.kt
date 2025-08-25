package com.example.food_order.utils.state

import com.example.food_order.data.model.common.Category
import com.example.food_order.data.model.common.FoodItem
import com.example.food_order.data.model.common.Restaurant

data class CustomerHomeUiState(
    val isLoading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val restaurants: List<Restaurant> = emptyList(),
    val popularItems: List<FoodItem> = emptyList(),
    val error: String? = null
)