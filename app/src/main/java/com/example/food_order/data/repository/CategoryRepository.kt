package com.example.food_order.data.repository

import com.example.food_order.data.api.CategoryApiServices
import com.example.food_order.data.model.common.Category
import com.example.food_order.data.model.common.Restaurant
import com.example.food_order.data.model.response.BaseResponse
import com.example.food_order.data.model.response.CategoriesResponse
import com.example.food_order.data.model.response.RestaurantsResponse
import com.example.food_order.utils.extension.parseError

class CategoryRepository(
    private val categoryApiServices: CategoryApiServices
) {


    suspend fun getAllCategories(): Result<List<Category>> {
        return try {
            val response = categoryApiServices.getAllCategories()

            if (response.isSuccessful) {

                val categories = response.body()?.data
                if (categories != null) {
                    Result.success(categories)
                } else {

                    Result.failure(Exception("Response successful but data is null"))
                }
            } else {

                val errorMessage = response.errorBody()?.parseError()
                Result.failure(
                    Exception(errorMessage ?: "Failed to fetch categories: ${response.code()}")
                )
            }
        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    suspend fun getRestaurantsByCategoryId(categoryId: String): Result<List<Restaurant>> {
        return try {
            val response = categoryApiServices.getRestaurantsByCategoryId(categoryId)

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
                    Exception(errorMessage ?: "Failed to fetch restaurants: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
