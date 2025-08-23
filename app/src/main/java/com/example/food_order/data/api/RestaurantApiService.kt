package com.example.food_order.data.api

import com.example.food_order.data.model.response.OwnerRestaurant
import com.example.food_order.data.model.response.OwnerRestaurantsResponse
import retrofit2.Response
import retrofit2.http.GET

interface RestaurantApiService {
    // BE: GET /restaurants/owner/me  (JWT lấy từ AuthInterceptor)
    @GET("restaurants/owner/me")
    suspend fun getMyRestaurants(): Response<OwnerRestaurantsResponse>
}
