package com.example.food_order.ui.customer.home.detail.categoryDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.model.common.Restaurant
import com.example.food_order.data.repository.CategoryRepository
import com.example.food_order.utils.state.CategoryDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CategoryDetailViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryDetailUiState())
    val uiState: StateFlow<CategoryDetailUiState> = _uiState.asStateFlow()

    fun fetchRestaurantsByCategoryId(categoryId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = categoryRepository.getRestaurantsByCategoryId(categoryId)

            result.onSuccess { restaurantList ->
                _uiState.update {
                    it.copy(isLoading = false, restaurants = restaurantList)
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(isLoading = false, error = exception.message)
                }
            }
        }
    }


    fun setCategoryName(name: String) {
        _uiState.update { it.copy(categoryName = name) }
    }
}