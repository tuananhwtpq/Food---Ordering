package com.example.food_order.ui.customer.home


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.api.RestaurantApiService
import com.example.food_order.data.model.common.Category
import com.example.food_order.data.model.common.FoodItem
import com.example.food_order.data.model.common.Restaurant
import com.example.food_order.data.repository.CategoryRepository
import com.example.food_order.data.repository.FakeFoodRepository
import com.example.food_order.data.repository.MenuItem
import com.example.food_order.data.repository.RestaurantRepository
import com.example.food_order.manager.SessionManager
import com.example.food_order.utils.state.CustomerHomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomerHomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val restaurantRepository: RestaurantRepository,
    private val restaurantApiService: RestaurantApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerHomeUiState())
    val uiState: StateFlow<CustomerHomeUiState> = _uiState.asStateFlow()

    private val _restaurantId = MutableStateFlow("7b8d53be-f03d-4742-a9c2-15ba95aa35a3")
    val restaurantId: StateFlow<String> = _restaurantId.asStateFlow()


    init {
        fetchAllData()
    }

    fun fetchAllData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val lat = sessionManager.fetchLatitude() ?: 21.053731
            val lon = sessionManager.fetchLongitude() ?: 105.7351068
            val restaurantId = _restaurantId.value

            var categoryList: List<Category> = emptyList()
            var restaurantList: List<Restaurant> = emptyList()
            var popularItemList: List<MenuItem> = emptyList()
            var errorMessage: String? = null

            try {
                val categoriesResponse = categoryRepository.getAllCategories()
                categoryList = categoriesResponse.getOrThrow()
            } catch (e: Exception) {
                errorMessage = "Failed to fetch categories: ${e.message}"
                Log.e("CustomerHomeViewModel", errorMessage, e)
            }

            try {
                val restaurantsResponse = restaurantRepository.getNearbyRestaurants(lat, lon)
                restaurantList = restaurantsResponse.getOrThrow()
            } catch (e: Exception) {
                if (errorMessage == null) {
                    errorMessage = "Failed to fetch restaurants: ${e.message}"
                    Log.e("CustomerHomeViewModel", errorMessage, e)
                }
            }

            try {
                val response = restaurantApiService.getRestaurantMenu(restaurantId)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.data != null) {
                        popularItemList = body.data
                    } else {
                        errorMessage = "No menu items found for restaurant"
                        Log.e("CustomerHomeViewModel", errorMessage)
                    }
                } else {
                    errorMessage = "Failed to fetch menu items: HTTP ${response.code()}"
                    Log.e("CustomerHomeViewModel", errorMessage)
                }
            } catch (e: Exception) {
                if (errorMessage == null) {
                    errorMessage = "Failed to fetch popular items: ${e.message}"
                    Log.e("CustomerHomeViewModel", errorMessage, e)
                }
            }

            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    categories = categoryList,
                    restaurants = restaurantList,
                    popularItems = popularItemList,
                    error = errorMessage
                )
            }
        }
    }

//    fun searchByName(query: String) {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true, error = null) }
//
//            try {
//                val response = restaurantRepository.searchByName(query)
//                if (response.isSuccess) {
//                    val searchResult = response.getOrThrow()
//                    _uiState.update { currentState ->
//                        currentState.copy(
//                            isLoading = false,
//                            searchResults = CustomerHomeUiState.SearchResult(
//                                restaurants = searchResult.restaurants,
//                                menuItems = searchResult.menuItems
//                            ),
//                            error = null
//                        )
//                    }
//                } else {
//                    _uiState.update { currentState ->
//                        currentState.copy(
//                            isLoading = false,
//                            error = response.exceptionOrNull()?.message ?: "Search failed"
//                        )
//                    }
//                }
//            } catch (e: Exception) {
//                _uiState.update { currentState ->
//                    currentState.copy(
//                        isLoading = false,
//                        error = "Search failed: ${e.message}"
//                    )
//                }
//                Log.e("CustomerHomeViewModel", "Search failed: ${e.message}", e)
//            }
//        }
//    }

}