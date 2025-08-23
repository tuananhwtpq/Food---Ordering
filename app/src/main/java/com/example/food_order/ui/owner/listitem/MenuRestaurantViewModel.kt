// MenuRestaurantViewModel.kt
package com.example.food_order.ui.owner.listitem

import android.content.Context
import androidx.lifecycle.*
import com.example.food_order.data.repository.*
import com.example.food_order.data.model.request.MenuRequest
import com.example.food_order.di.RetrofitInstance
import com.example_food_order.data.repository.MenuItem
import kotlinx.coroutines.launch
import com.example.food_order.data.repository.MenuApiService

class MenuRestaurantViewModel(
    private val restaurantId: String,
    private val repo: IMenuRepository
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _items = MutableLiveData<List<MenuItem>>(emptyList())
    val items: LiveData<List<MenuItem>> = _items

    fun loadMenu() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            when (val res = repo.fetchMenu(restaurantId)) {
                is Result.Success -> _items.value = res.data
                is Result.Failure -> _error.value = res.message ?: "Lỗi tải menu"
            }
            _loading.value = false
        }
    }

    fun create(body: MenuRequest, onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            _loading.value = true
            when (val res = repo.create(restaurantId, body)) {
                is Result.Success -> {
                    _items.value = _items.value.orEmpty() + res.data
                    onDone?.invoke()
                }
                is Result.Failure -> _error.value = res.message ?: "Tạo món thất bại"
            }
            _loading.value = false
        }
    }

    fun update(itemId: String, body: MenuRequest, onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            _loading.value = true
            when (val res = repo.update(restaurantId, itemId, body)) {
                is Result.Success -> {
                    val current = _items.value.orEmpty().toMutableList()
                    val idx = current.indexOfFirst { it.id == itemId }
                    if (idx >= 0) {
                        current[idx] = res.data
                        _items.value = current
                    }
                    onDone?.invoke()
                }
                is Result.Failure -> _error.value = res.message ?: "Cập nhật thất bại"
            }
            _loading.value = false
        }
    }

    fun delete(itemId: String, onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            _loading.value = true
            when (val res = repo.delete(restaurantId, itemId)) {
                is Result.Success -> {
                    _items.value = _items.value.orEmpty().filterNot { it.id == itemId }
                    onDone?.invoke()
                }
                is Result.Failure -> _error.value = res.message ?: "Xóa thất bại"
            }
            _loading.value = false
        }
    }

    companion object {
        fun provideFactory(
            context: Context,
            restaurantId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                // 1) Tạo service **đúng kiểu** và **đúng tên**
                val service: MenuApiService =
                    RetrofitInstance.createAuthorizedServiceGeneric<MenuApiService>(
                        context.applicationContext
                    )

                // 2) Tạo repository từ service (dùng đúng class repo bạn đang có)
                val repo = MenuRepository(service)

                // 3) Trả về ViewModel
                if (modelClass.isAssignableFrom(MenuRestaurantViewModel::class.java)) {
                    return MenuRestaurantViewModel(restaurantId, repo) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
