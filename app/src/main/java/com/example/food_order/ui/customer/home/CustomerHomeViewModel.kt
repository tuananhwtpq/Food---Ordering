package com.example.food_order.ui.customer.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.model.common.Category
import com.example.food_order.data.repository.CategoryRepository
import com.example.food_order.data.repository.FakeFoodRepository
import com.example.food_order.utils.state.CustomerHomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomerHomeViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerHomeUiState())
    val uiState: StateFlow<CustomerHomeUiState> = _uiState.asStateFlow()

    init {
        fetchAllData()
    }

    fun fetchAllData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val categoriesResult: Result<List<Category>> = categoryRepository.getAllCategories()

            categoriesResult.onSuccess { categoryList ->
                _uiState.update { currentState ->
                    currentState.copy(categories = categoryList)
                }
            }.onFailure { exception ->
                _uiState.update { currentState ->
                    currentState.copy(error = exception.message)
                }
            }

            val fakeRestaurants = FakeFoodRepository.getFeaturedRestaurants()
            val fakePopularItems = FakeFoodRepository.getPopularItems()
            _uiState.update { currentState ->
                currentState.copy(
                    restaurants = fakeRestaurants,
                    popularItems = fakePopularItems
                )
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}