package com.example.food_order.ui.customer.home.detail.restaurantDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.repository.RestaurantRepository
import com.example.food_order.utils.state.RestaurantDetailUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantDetailUiState())
    val uiState: StateFlow<RestaurantDetailUiState> = _uiState

    fun fetchRestaurantData(restaurantId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val detailDeferred = async { restaurantRepository.getRestaurantDetails(restaurantId) }
            val menuDeferred = async { restaurantRepository.getRestaurantMenu(restaurantId) }

            val detailResult = detailDeferred.await()
            val menuResult = menuDeferred.await()

            detailResult.onSuccess { restaurant ->
                menuResult.onSuccess { menu ->
                    _uiState.update {
                        it.copy(isLoading = false, restaurant = restaurant, menuItems = menu)
                    }
                }.onFailure {
                    _uiState.update { it.copy(isLoading = false, error = it.error) }
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false, error = it.error) }
            }
        }
    }
}