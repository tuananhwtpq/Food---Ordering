package com.example.food_order.data.api
// ^ dùng đúng package dự án (không có gạch dưới)

import com.example.food_order.data.model.request.MenuRequest
import com.example.food_order.data.model.response.MenuListResponse
import retrofit2.Response
import retrofit2.http.*

data class CreateMenuItemResult(val id: String, val message: String)
data class SimpleMessage(val message: String)

interface MenuApiService {

    @GET("restaurants/{id}/menu")
    suspend fun getMenu(
        @Path("id") restaurantId: String
    ): Response<MenuListResponse>        // <-- { foodItems: [...] }

    @POST("restaurants/{id}/menu")
    suspend fun createMenuItem(
        @Path("id") restaurantId: String,
        @Body body: MenuRequest
    ): Response<CreateMenuItemResult>    // <-- { id, message }

    @PATCH("menu/{itemId}")
    suspend fun updateMenuItem(
        @Path("itemId") itemId: String,
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Response<SimpleMessage>           // <-- { message }

    @DELETE("menu/{itemId}")
    suspend fun deleteMenuItem(
        @Path("itemId") itemId: String
    ): Response<SimpleMessage>           // <-- { message }
}
