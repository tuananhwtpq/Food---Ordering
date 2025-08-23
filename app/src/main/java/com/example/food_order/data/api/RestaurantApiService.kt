package com.example.food_order.data.api

import retrofit2.Response
import retrofit2.http.GET

interface RestaurantApiService {
    @GET("restaurants/owner/me")           // không có dấu `/` đầu, để Retrofit nối với BASE_URL
    suspend fun getMyRestaurants(): Response<OwnerRestaurantsResponse>
}

data class OwnerRestaurantsResponse(val data: List<OwnerRestaurant>)

data class OwnerRestaurant(
    val id: String,
    val ownerId: String,
    val name: String,
    val address: String?,
    val imageUrl: String?,
    val categoryId: String?,
    val latitude: Double?,
    val longitude: Double?,
    val createdAt: String?
)

