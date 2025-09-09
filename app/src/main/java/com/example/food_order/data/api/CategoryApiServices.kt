package com.example.food_order.data.api

import com.example.food_order.data.model.common.Category
import com.example.food_order.data.model.common.Restaurant
import com.example.food_order.data.model.response.BaseResponse
import com.example.food_order.data.model.response.CategoriesResponse
import com.example.food_order.data.model.response.RestaurantsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryApiServices {

    @GET("categories")
    suspend fun getAllCategories(): Response<BaseResponse<List<Category>>>

    @GET("categories/{id}/restaurants")
    suspend fun getRestaurantsByCategoryId(
        @Path("id") categoryId: String
    ): Response<BaseResponse<List<Restaurant>>>
}