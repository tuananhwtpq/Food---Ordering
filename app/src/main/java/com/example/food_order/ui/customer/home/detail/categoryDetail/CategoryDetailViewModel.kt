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

            val fakeRestaurants = listOf(
                Restaurant(
                    id = UUID.randomUUID().toString(),
                    ownerId = UUID.randomUUID().toString(),
                    name = "Quán hạnh phúc",
                    address = "123 Đường Lập Trình, Quận Code, TP. Android",
                    categoryId = categoryId,
                    latitude = 10.77,
                    longitude = 106.69,
                    createdAt = "18/8/2025",
                    distance = 1.5,
                    imageUrl = "https://res.cloudinary.com/dsizusoj0/image/upload/v1755965937/Banh-my-pho-5-2-1199x800_pqmhig.jpg"
                ),
                Restaurant(
                    id = UUID.randomUUID().toString(),
                    ownerId = UUID.randomUUID().toString(),
                    name = "Quán ăn Vui Vẻ",
                    address = "456 Đường Debug, Phường Bug, TP. Android",
                    categoryId = categoryId,
                    latitude = 10.78,
                    longitude = 106.70,
                    createdAt = "18/8/2025",
                    distance = 2.8,
                    imageUrl = "https://res.cloudinary.com/dsizusoj0/image/upload/v1755965938/Pho-Vui-Hang-Giay_gwow8h.jpg"
                )
            )

            _uiState.update {
                it.copy(isLoading = false, restaurants = fakeRestaurants)
            }


//            val result = categoryRepository.getRestaurantsByCategoryId(categoryId)
//
//            result.onSuccess { restaurantList ->
//                _uiState.update {
//                    it.copy(isLoading = false, restaurants = restaurantList)
//                }
//            }.onFailure { exception ->
//                _uiState.update {
//                    it.copy(isLoading = false, error = exception.message)
//                }
//            }
        }
    }


    fun setCategoryName(name: String) {
        _uiState.update { it.copy(categoryName = name) }
    }
}