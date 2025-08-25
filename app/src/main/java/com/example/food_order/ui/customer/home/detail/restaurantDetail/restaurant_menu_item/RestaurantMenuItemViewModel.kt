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

    private val _showClearCartDialog = MutableLiveData<Pair<String, String>>()
    val showClearCartDialog: LiveData<Pair<String, String>> = _showClearCartDialog

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

//    fun addToCart(restaurantId: String, menuItemId: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = AddToCartRequest(
//                restaurantId = restaurantId,
//                menuItemId = menuItemId,
//                quantity = _quantity.value ?: 1
//            )
//            val result = cartRepository.addToCart(request)
//            _addToCartStatus.postValue(result)
//            _isLoading.postValue(false)
//        }
//    }

    fun onAddToCartClicked(newItemRestaurantId: String, newItemMenuItemId: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val cartResult = cartRepository.getCart()

            cartResult.onSuccess { cartResponse ->
                val currentCartItems = cartResponse.items
                if (currentCartItems.isEmpty() || currentCartItems.first().restaurantId == newItemRestaurantId) {
                    addToCartInternal(newItemRestaurantId, newItemMenuItemId)
                } else {
                    _isLoading.postValue(false)
                    _showClearCartDialog.postValue(newItemRestaurantId to newItemMenuItemId)
                }
            }.onFailure {
                _addToCartStatus.postValue(Result.failure(it))
                _isLoading.postValue(false)
            }
        }
    }

    fun clearCartAndAddItem(restaurantId: String, menuItemId: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val clearResult = cartRepository.clearCart()
            if (clearResult.isSuccess) {
                addToCartInternal(restaurantId, menuItemId)
            } else {
                _addToCartStatus.postValue(
                    Result.failure(
                        clearResult.exceptionOrNull() ?: Exception("Không thể xóa giỏ hàng")
                    )
                )
                _isLoading.postValue(false)
            }
        }
    }

    private suspend fun addToCartInternal(restaurantId: String, menuItemId: String) {
        val request = AddToCartRequest(
            restaurantId = restaurantId,
            menuItemId = menuItemId,
            quantity = _quantity.value ?: 1
        )
        val result = cartRepository.addToCart(request)
        _addToCartStatus.postValue(result)
    }
}