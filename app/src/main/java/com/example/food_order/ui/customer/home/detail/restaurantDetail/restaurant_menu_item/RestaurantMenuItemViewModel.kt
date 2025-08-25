package com.example.food_order.ui.customer.home.detail.restaurantDetail.restaurant_menu_item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.model.common.AddToCartRequest
import com.example.food_order.data.repository.CartRepository
import com.example.food_order.data.repository.MenuItem
import com.example.food_order.data.repository.RestaurantRepository
import kotlinx.coroutines.launch

class RestaurantMenuItemViewModel(
    private val restaurantRepository: RestaurantRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _menuItemDetails = MutableLiveData<Result<MenuItem>>()
    val menuItemDetails: LiveData<Result<MenuItem>> = _menuItemDetails

    private val _quantity = MutableLiveData<Int>(1)
    val quantity: LiveData<Int> = _quantity

    private val _addToCartStatus = MutableLiveData<Result<Unit>>()
    val addToCartStatus: LiveData<Result<Unit>> = _addToCartStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getMenuItemDetails(menuItemId: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val result = restaurantRepository.getMenuItemDetails(menuItemId)
            _menuItemDetails.postValue(result)
            _isLoading.postValue(false)
        }
    }


    fun incrementQuantity() {
        val currentQuantity = _quantity.value ?: 1
        _quantity.value = currentQuantity + 1
    }

    fun decrementQuantity() {
        val currentQuantity = _quantity.value ?: 1
        if (currentQuantity > 1) {
            _quantity.value = currentQuantity - 1
        }
    }

    fun addToCart(restaurantId: String, menuItemId: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = AddToCartRequest(
                restaurantId = restaurantId,
                menuItemId = menuItemId,
                quantity = _quantity.value ?: 1
            )
            val result = cartRepository.addToCart(request)
            _addToCartStatus.postValue(result)
            _isLoading.postValue(false)
        }
    }
}