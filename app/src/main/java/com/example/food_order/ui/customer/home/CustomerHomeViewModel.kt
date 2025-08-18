package com.example.food_order.ui.customer.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.food_order.data.model.common.Category
import com.example.food_order.data.model.common.FoodItem
import com.example.food_order.data.model.common.Restaurant
import com.example.food_order.data.repository.FakeFoodRepository

class CustomerHomeViewModel : ViewModel() {

    // LiveData cho danh mục
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    // LiveData cho nhà hàng
    private val _restaurants = MutableLiveData<List<Restaurant>>()
    val restaurants: LiveData<List<Restaurant>> get() = _restaurants

    // LiveData cho món ăn phổ biến
    private val _popularItems = MutableLiveData<List<FoodItem>>()
    val popularItems: LiveData<List<FoodItem>> get() = _popularItems

    // Hàm này được gọi từ Fragment để bắt đầu tải dữ liệu
    fun loadAllData() {
        _categories.value = FakeFoodRepository.getCategories()
        _restaurants.value = FakeFoodRepository.getFeaturedRestaurants()
        _popularItems.value = FakeFoodRepository.getPopularItems()
    }
}