package com.example.food_order.ui.customer.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.model.common.CartResponse
import com.example.food_order.data.model.common.UpdateCartItemRequest
import com.example.food_order.data.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartResponse = MutableLiveData<Result<CartResponse>>()
    val cartResponse: LiveData<Result<CartResponse>> = _cartResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _itemUpdateStatus = MutableLiveData<Result<Unit>>()
    val itemUpdateStatus: LiveData<Result<Unit>> = _itemUpdateStatus

//    init {
//        getCartDetails()
//    }

    fun getCartDetails() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val result = cartRepository.getCart()
            _cartResponse.postValue(result)
            _isLoading.postValue(false)
        }
    }

    fun updateCartItemQuantity(cartItemId: String, newQuantity: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = UpdateCartItemRequest(cartItemId, newQuantity)
            val result = cartRepository.updateCartItem(request)
            _itemUpdateStatus.postValue(result)
            if (result.isSuccess) {
                getCartDetails()
            } else {
                _isLoading.postValue(false)
            }
        }
    }

    fun removeCartItem(cartItemId: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val result = cartRepository.removeCartItem(cartItemId)
            _itemUpdateStatus.postValue(result)
            if (result.isSuccess) {
                getCartDetails()
            } else {
                _isLoading.postValue(false)
            }
        }
    }
}