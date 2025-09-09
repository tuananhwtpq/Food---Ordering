package com.example.food_order.data.repository

import com.example.food_order.data.api.CartApiService
import com.example.food_order.data.model.common.AddToCartRequest
import com.example.food_order.data.model.common.CartResponse
import com.example.food_order.data.model.common.UpdateCartItemRequest
import com.example.food_order.utils.extension.parseError

class CartRepository(
    private val apiService: CartApiService
) {

    suspend fun getCart(): Result<CartResponse> {
        return try {
            val response = apiService.getCart()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                Result.failure(Exception(response.errorBody()?.parseError() ?: "Get cart failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addToCart(request: AddToCartRequest): Result<Unit> {
        return try {
            val response = apiService.addToCart(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.parseError() ?: "Add to cart failed"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCartItem(request: UpdateCartItemRequest): Result<Unit> {
        return try {
            val response = apiService.updateCartItem(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.parseError() ?: "Update cart item failed"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeCartItem(cartItemId: String): Result<Unit> {
        return try {
            val response = apiService.removeCartItem(cartItemId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.parseError() ?: "Remove cart item failed"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearCart(): Result<Unit> {
        return try {
            val response = apiService.clearCart()
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.parseError() ?: "Clear cart failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}