package com.example.food_order.data.api

import com.example.food_order.data.model.common.AddToCartRequest
import com.example.food_order.data.model.common.CartResponse
import com.example.food_order.data.model.common.UpdateCartItemRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CartApiService {

    @GET("cart")
    suspend fun getCart(): Response<CartResponse>

    @POST("cart")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<Unit>

    @PATCH("cart")
    suspend fun updateCartItem(@Body request: UpdateCartItemRequest): Response<Unit>

    @DELETE("cart/{cartItemId}")
    suspend fun removeCartItem(@Path("cartItemId") cartItemId: String): Response<Unit>

    @DELETE("cart")
    suspend fun clearCart(): Response<Unit>
}