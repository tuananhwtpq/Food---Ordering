// MenuRepository.kt
package com.example.food_order.data.repository


import com.example.food_order.data.model.request.MenuRequest
import com.example.food_order.data.model.response.MenuResponse
import com.example_food_order.data.repository.MenuItem
import retrofit2.HttpException
import java.io.IOException

// UI model MenuItem đã có trong MenuDataSource.kt (cùng package), import để dùng
// import com.example.food_order.data.repository.MenuItem  // (Android Studio sẽ tự add)

sealed class Result<out T> {
    data class Success<T>(val data: T): Result<T>()
    data class Failure(val code: Int?, val message: String?): Result<Nothing>()
}

interface IMenuRepository {
    suspend fun fetchMenu(restaurantId: String): Result<List<MenuItem>>
    suspend fun create(restaurantId: String, body: MenuRequest): Result<MenuItem>
    suspend fun update(restaurantId: String, itemId: String, body: MenuRequest): Result<MenuItem>
    suspend fun delete(restaurantId: String, itemId: String): Result<Unit>
}

class MenuRepository(
    private val api: MenuApiService
) : IMenuRepository {

    override suspend fun fetchMenu(restaurantId: String): Result<List<MenuItem>> {
        return try {
            val resp = api.getMenu(restaurantId)
            if (resp.isSuccessful) {
                val list = resp.body().orEmpty().map { it.toDomain() }
                Result.Success(list)
            } else {
                Result.Failure(resp.code(), resp.errorBody()?.string())
            }
        } catch (e: IOException) {
            Result.Failure(null, e.message)
        } catch (e: HttpException) {
            Result.Failure(e.code(), e.message())
        }
    }

    override suspend fun create(restaurantId: String, body: MenuRequest): Result<MenuItem> {
        return try {
            val resp = api.createMenuItem(restaurantId, body)
            if (resp.isSuccessful) {
                Result.Success(resp.body()!!.toDomain())
            } else {
                Result.Failure(resp.code(), resp.errorBody()?.string())
            }
        } catch (e: IOException) {
            Result.Failure(null, e.message)
        } catch (e: HttpException) {
            Result.Failure(e.code(), e.message())
        }
    }

    override suspend fun update(
        restaurantId: String,
        itemId: String,
        body: MenuRequest
    ): Result<MenuItem> {
        return try {
            val resp = api.updateMenuItem(restaurantId, itemId, body)
            if (resp.isSuccessful) {
                Result.Success(resp.body()!!.toDomain())
            } else {
                Result.Failure(resp.code(), resp.errorBody()?.string())
            }
        } catch (e: IOException) {
            Result.Failure(null, e.message)
        } catch (e: HttpException) {
            Result.Failure(e.code(), e.message())
        }
    }

    override suspend fun delete(restaurantId: String, itemId: String): Result<Unit> {
        return try {
            val resp = api.deleteMenuItem(restaurantId, itemId)
            if (resp.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Failure(resp.code(), resp.errorBody()?.string())
            }
        } catch (e: IOException) {
            Result.Failure(null, e.message)
        } catch (e: HttpException) {
            Result.Failure(e.code(), e.message())
        }
    }
}

/** Map DTO -> UI model đang dùng trong adapter */
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
