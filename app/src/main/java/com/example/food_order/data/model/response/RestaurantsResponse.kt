package com.example.food_order.data.model.response

import com.google.gson.annotations.SerializedName

data class RestaurantsResponse(

    @SerializedName("id")
    val id: String,

    @SerializedName("ownerId")
    val ownerId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("address")
    val address: String?,

    @SerializedName("categoryId")
    val categoryId: String,

    @SerializedName("latitude")
    val latitude: Double?,

    @SerializedName("longitude")
    val longitude: Double?,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("distance")
    val distance: Double?,
    @SerializedName("imageUrl")
    val imageUrl: String?
)
