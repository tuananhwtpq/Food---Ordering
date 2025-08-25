package com.example.food_order.utils.state

import com.example.food_order.data.model.common.Restaurant

data class CategoryDetailUiState(
    val isLoading: Boolean = true,
    val restaurants: List<Restaurant> = emptyList(),
    val error: String? = null,
    val categoryName: String = ""
)