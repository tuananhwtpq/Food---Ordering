package com.example.food_order.ui.owner.listitem

import android.content.Context
import androidx.lifecycle.*
import com.example.food_order.data.api.MenuApiService
import com.example.food_order.data.model.request.MenuRequest
import com.example.food_order.data.repository.AppResult
import com.example.food_order.data.repository.IMenuRepository
import com.example.food_order.data.repository.MenuRepository
import com.example.food_order.di.RetrofitInstance
import com.example_food_order.data.repository.MenuItem
import kotlinx.coroutines.launch

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

    /** Load toàn bộ menu của nhà hàng */
    fun loadMenu() = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        when (val res = repo.fetchMenu(restaurantId)) {
            is AppResult.Success -> _items.value = res.data
            is AppResult.Failure -> _error.value = res.message ?: "Không tải được menu"
        }
        _loading.value = false
    }

    /** Tạo món mới -> BE trả {id,message}; FE chỉ cần refresh list */
    fun create(body: MenuRequest, onDone: (() -> Unit)? = null) = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        when (val res = repo.create(restaurantId, body)) {
            is AppResult.Success -> {
                loadMenu()          // refresh từ server để đồng bộ
                onDone?.invoke()
            }
            is AppResult.Failure -> _error.value = res.message ?: "Tạo món thất bại"
        }
        _loading.value = false
    }

    /** Cập nhật món -> BE dùng PATCH /menu/{itemId} với map field */
    fun update(itemId: String, body: MenuRequest, onDone: (() -> Unit)? = null) = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        val patch = body.toPatchMap()
        when (val res = repo.update(itemId, patch)) {
            is AppResult.Success -> {
                loadMenu()          // refresh lại danh sách
                onDone?.invoke()
            }
            is AppResult.Failure -> _error.value = res.message ?: "Cập nhật món thất bại"
        }
        _loading.value = false
    }

    /** Xoá món -> refresh list */
    fun delete(itemId: String, onDone: (() -> Unit)? = null) = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        when (val res = repo.delete(itemId)) {
            is AppResult.Success -> {
                // có thể lọc local cho mượt, nhưng để chắc chắn thì reload
                loadMenu()
                onDone?.invoke()
            }
            is AppResult.Failure -> _error.value = res.message ?: "Xoá món thất bại"
        }
        _loading.value = false
    }

    companion object {
        /** Factory tạo VM với Retrofit service sẵn, tránh đụng RetrofitInstance/Interceptor */
        fun provideFactory(context: Context, restaurantId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    require(restaurantId.isNotBlank()) { "restaurantId is blank" }
                    val service: MenuApiService =
                        RetrofitInstance.createAuthorizedServiceGeneric<MenuApiService>(context.applicationContext)
                    val repo = MenuRepository(service)
                    if (modelClass.isAssignableFrom(MenuRestaurantViewModel::class.java)) {
                        return MenuRestaurantViewModel(restaurantId, repo) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
    }
}

/** Map từ MenuRequest sang body PATCH (chỉ gửi field bạn cho phép cập nhật) */
private fun MenuRequest.toPatchMap(): Map<String, Any?> {
    // Nếu MenuRequest của bạn có các field khác, thêm vào đây cho khớp.
    val m = mutableMapOf<String, Any?>()
    m["name"] = name
    m["description"] = description
    m["price"] = price
    m["imageUrl"] = imageUrl
    m["arModelUrl"] = arModelUrl
    return m.filterValues { it != null }  // bỏ qua null để PATCH partial
}
