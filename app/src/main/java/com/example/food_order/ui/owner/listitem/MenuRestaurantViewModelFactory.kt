package com.example.food_order.ui.owner.listitem

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food_order.data.api.MenuApiService
import com.example.food_order.data.repository.MenuRepository
import com.example.food_order.di.RetrofitInstance
import com.example.food_order.manager.SessionManager


/**
 * Factory tạo MenuRestaurantViewModel với restaurantId “chắc chắn có”.
 * - Ưu tiên id truyền trực tiếp (explicitRestaurantId)
 * - Nếu null, fallback từ SessionManager (selectedRestaurantId đã lưu sau login)
 * - Nếu vẫn null -> throw IllegalStateException để dev biết phải chọn nhà hàng trước.
 */
class MenuRestaurantViewModelFactory(
    private val context: Context,
    private val explicitRestaurantId: String? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val service: MenuApiService =
            RetrofitInstance.createAuthorizedServiceGeneric<MenuApiService>(context.applicationContext)
        val repo = MenuRepository(service)

        val effectiveRestaurantId = explicitRestaurantId
            ?: SessionManager(context.applicationContext).fetchSelectedRestaurantId()
            ?: throw IllegalStateException(
                "RestaurantId is null. Owner must select a restaurant before opening menu."
            )

        if (modelClass.isAssignableFrom(MenuRestaurantViewModel::class.java)) {
            return MenuRestaurantViewModel(effectiveRestaurantId, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
