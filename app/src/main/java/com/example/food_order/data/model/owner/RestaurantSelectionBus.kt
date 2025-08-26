package com.example.food_order.data.model.owner

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object RestaurantSelectionBus {
    private val _selectedId = MutableStateFlow<String?>(null)
    val selectedId: StateFlow<String?> = _selectedId

    fun update(id: String?) {
        _selectedId.value = id
    }
}
