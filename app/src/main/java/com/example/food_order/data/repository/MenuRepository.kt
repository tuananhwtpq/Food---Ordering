package com.example.food_order.data.repository

import com.example.food_order.data.api.MenuApiService
import com.example.food_order.data.model.request.MenuRequest
import com.example.food_order.data.model.response.MenuResponse
import com.example_food_order.data.repository.MenuItem
import retrofit2.HttpException
import java.io.IOException

sealed class AppResult<out T> {
    data class Success<T>(val data: T): AppResult<T>()
    data class Failure(val code: Int?, val message: String?): AppResult<Nothing>()
}

interface IMenuRepository {
    suspend fun fetchMenu(restaurantId: String): AppResult<List<MenuItem>>
    suspend fun create(restaurantId: String, body: MenuRequest): AppResult<Unit>
    suspend fun update(itemId: String, updatedFields: Map<String, Any?>): AppResult<Unit>
    suspend fun delete(itemId: String): AppResult<Unit>
}

class MenuRepository(
    private val api: MenuApiService
) : IMenuRepository {

    override suspend fun fetchMenu(restaurantId: String): AppResult<List<MenuItem>> {
        return try {
            val resp = api.getMenu(restaurantId)
            if (resp.isSuccessful) {
                val list = resp.body()?.foodItems.orEmpty().map { it.toDomain() } // <-- đổi sang foodItems
                AppResult.Success(list)
            } else {
                AppResult.Failure(resp.code(), resp.errorBody()?.string())
            }
        } catch (e: IOException) {
            AppResult.Failure(null, e.message)
        } catch (e: HttpException) {
            AppResult.Failure(e.code(), e.message())
        }
    }

    override suspend fun create(restaurantId: String, body: MenuRequest): AppResult<Unit> {
        return try {
            val resp = api.createMenuItem(restaurantId, body)
            if (resp.isSuccessful) {
                // BE trả {id, message} -> FE chỉ cần refresh list, không cần parse sâu
                AppResult.Success(Unit)
            } else {
                AppResult.Failure(resp.code(), resp.errorBody()?.string())
            }
        } catch (e: IOException) {
            AppResult.Failure(null, e.message)
        } catch (e: HttpException) {
            AppResult.Failure(e.code(), e.message())
        }
    }

    override suspend fun update(itemId: String, updatedFields: Map<String, Any?>): AppResult<Unit> {
        return try {
            val resp = api.updateMenuItem(itemId, updatedFields)
            if (resp.isSuccessful) AppResult.Success(Unit)
            else AppResult.Failure(resp.code(), resp.errorBody()?.string())
        } catch (e: IOException) {
            AppResult.Failure(null, e.message)
        } catch (e: HttpException) {
            AppResult.Failure(e.code(), e.message())
        }
    }

    override suspend fun delete(itemId: String): AppResult<Unit> {
        return try {
            val resp = api.deleteMenuItem(itemId)
            if (resp.isSuccessful) AppResult.Success(Unit)
            else AppResult.Failure(resp.code(), resp.errorBody()?.string())
        } catch (e: IOException) {
            AppResult.Failure(null, e.message)
        } catch (e: HttpException) {
            AppResult.Failure(e.code(), e.message())
        }
    }
}

// map DTO -> UI model bạn đang dùng trong adapter
private fun MenuResponse.toDomain() = MenuItem(
    id = this.id,
    restaurantId = this.restaurantId,
    name = this.name,
    description = this.description,
    price = this.price,
    imageUrl = this.imageUrl,
    arModelUrl = this.arModelUrl,
    createdAt = this.createdAt
)
