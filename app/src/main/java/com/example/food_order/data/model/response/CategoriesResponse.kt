package com.example.food_order.data.model.response

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("imageUrl")
    val imageUrl: String?,

    @SerializedName("createdAt")
    val createdAt: String,
)
