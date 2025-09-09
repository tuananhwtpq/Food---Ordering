package com.example.food_order.data.api

import com.example.food_order.data.model.common.Restaurant
import com.example.food_order.data.model.response.BaseResponse
import com.example.food_order.data.repository.MenuItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestaurantApiService {

    @GET("restaurants/{id}")
    suspend fun getRestaurantDetails(
        @Path("id") restaurantId: String
    ): Response<BaseResponse<Restaurant>>

    @GET("restaurants/{id}/menu")
    suspend fun getRestaurantMenu(
        @Path("id") restaurantId: String
    ): Response<BaseResponse<List<MenuItem>>>

    @GET("restaurants")
    suspend fun getNearbyRestaurants(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("categoryId") categoryId: String? = null
    ): Response<BaseResponse<List<Restaurant>>>

    @GET("menu/{itemId}")
    suspend fun getMenuItemDetails(
        @Path("itemId") menuItemId: String
    ): Response<BaseResponse<MenuItem>>

    @GET("restaurants/search")
    suspend fun searchByName(
        @Query("query") query: String
    ): Response<BaseResponse<SearchResult>>

}

data class SearchResult(
    val restaurants: List<Restaurant>,
    val menuItems: List<MenuItem>
)