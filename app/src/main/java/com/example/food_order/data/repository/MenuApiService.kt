package com.example.food_order.data.repository


import com.example.food_order.data.model.request.MenuRequest
import com.example.food_order.data.model.response.MenuResponse
import retrofit2.Response
import retrofit2.http.*

interface MenuApiService {

    @GET("restaurants/{id}/menu")
    suspend fun getMenu(
        @Path("id") restaurantId: String
    ): Response<List<MenuResponse>>

    @POST("restaurants/{id}/menu")
    suspend fun createMenuItem(
        @Path("id") restaurantId: String,
        @Body body: MenuRequest
    ): Response<MenuResponse>

    @PUT("restaurants/{id}/menu/{itemId}")
    suspend fun updateMenuItem(
        @Path("id") restaurantId: String,
        @Path("itemId") itemId: String,
        @Body body: MenuRequest
    ): Response<MenuResponse>

    @DELETE("restaurants/{id}/menu/{itemId}")
    suspend fun deleteMenuItem(
        @Path("id") restaurantId: String,
        @Path("itemId") itemId: String
    ): Response<Unit>
}
