package com.example.food_order.ui.customer.home.detail.restaurantDetail.restaurant_menu_item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.repository.MenuItem
import com.example.food_order.data.repository.RestaurantRepository
import kotlinx.coroutines.launch

class RestaurantMenuItemViewModel(
    private val repository: RestaurantRepository
) : ViewModel() {

    private val _menuItemDetails = MutableLiveData<Result<MenuItem>>()
    val menuItemDetails: LiveData<Result<MenuItem>> = _menuItemDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getMenuItemDetails(menuItemId: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val result = repository.getMenuItemDetails(menuItemId)
                _menuItemDetails.postValue(result)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}