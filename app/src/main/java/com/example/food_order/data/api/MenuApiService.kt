package com.example.food_order.data.api

import com.example.food_order.data.model.request.MenuRequest
import com.example.food_order.data.model.request.UpdateMenuItemRequest
import com.example.food_order.data.model.response.MenuListResponse
import retrofit2.Response
import retrofit2.http.*

/** BE trả { "id": "...", "message": "..." } khi create */
data class CreateMenuItemResult(val id: String, val message: String)

/** BE trả { "message": "..." } khi update/delete */
data class SimpleMessage(val message: String)

interface MenuApiService {

    /** GET danh sách: { foodItems: [...] } */
    @GET("restaurants/{id}/menu")
    suspend fun getMenu(
        @Path("id") restaurantId: String
    ): Response<MenuListResponse>

    /** POST tạo món mới dưới restaurant: { id, message } */
    @POST("restaurants/{id}/menu")
    suspend fun createMenuItem(
        @Path("id") restaurantId: String,
        @Body body: MenuRequest
    ): Response<CreateMenuItemResult>

    @PATCH("menu/{itemId}")
    suspend fun updateMenuItem(
        @Path("itemId") itemId: String,
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Response<SimpleMessage>

    /** DELETE item theo id độc lập: { message } */
    @DELETE("menu/{itemId}")
    suspend fun deleteMenuItem(
        @Path("itemId") itemId: String
    ): Response<SimpleMessage>
}
