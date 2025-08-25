package com.example.food_order.ui.owner.listitem

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.food_order.data.api.MenuApiService
import com.example.food_order.data.model.request.MenuRequest
import com.example.food_order.data.model.request.UpdateMenuItemRequest
import com.example.food_order.data.repository.AppResult
import com.example.food_order.data.repository.IMenuRepository
import com.example.food_order.data.repository.MenuItem
import com.example.food_order.data.repository.MenuRepository
import com.example.food_order.di.RetrofitInstance
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

        // map sang UpdateMenuItemRequest đúng BE
        val updateBody = UpdateMenuItemRequest(
            name = body.name,
            description = body.description,
            price = body.price,
            imageUrl = body.imageUrl,
            // category, isAvailable nếu bạn có UI cho 2 trường này thì set thêm ở đây
        )
        val patch = body.toPatchMap()
        when (val res = repo.update(itemId, patch)) {
            is AppResult.Success -> {
                loadMenu()
                onDone?.invoke()
                Log.d("MenuPatch", "PATCH body=" + patch)

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

private fun MenuRequest.toPatchMap(): Map<String, Any?> {
    val m = mutableMapOf<String, Any?>(
        "name" to name,
        "description" to description,
        "price" to price,
        "imageUrl" to imageUrl,
        "arModelUrl" to arModelUrl
    )
    return m.filter { (_, v) ->
        when (v) {
            null -> false
            is String -> v.isNotBlank()
            else -> true
        }
    }
}
