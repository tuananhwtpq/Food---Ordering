package com.example.food_order.data.repository

import com.example.food_order.data.api.RestaurantApiService
import com.example.food_order.data.model.common.Restaurant
import com.example.food_order.utils.extension.parseError

class RestaurantRepository(
    private val apiService: RestaurantApiService
) {

    suspend fun getRestaurantDetails(restaurantId: String): Result<Restaurant> {
        return try {
            val response = apiService.getRestaurantDetails(restaurantId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.data != null) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception("Response body or data is null"))
                }
            } else {
                Result.failure(Exception("API call failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRestaurantMenu(restaurantId: String): Result<List<MenuItem>> {
        return try {
            val response = apiService.getRestaurantMenu(restaurantId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.data != null) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception("Response body or data is null"))
                }
            } else {
                Result.failure(Exception("API call failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNearbyRestaurants(
        latitude: Double,
        longitude: Double,
        categoryId: String? = null
    ): Result<List<Restaurant>> {
        return try {
            val response = apiService.getNearbyRestaurants(latitude, longitude, categoryId)
            if (response.isSuccessful) {
                val restaurants = response.body()?.data
                if (restaurants != null) {
                    Result.success(restaurants)
                } else {
                    Result.failure(Exception("Response successful but data is null"))
                }
            } else {
                val errorMessage = response.errorBody()?.parseError()
                Result.failure(
                    Exception(
                        errorMessage ?: "Failed to fetch nearby restaurants: ${response.code()}"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getMenuItemDetails(menuItemId: String): Result<MenuItem> {
        return try {
            val response = apiService.getMenuItemDetails(menuItemId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.data != null) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception("Response body or data is null for menu item"))
                }
            } else {
                val errorMessage = response.errorBody()?.parseError()
                Result.failure(
                    Exception(errorMessage ?: "API call failed with code: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}