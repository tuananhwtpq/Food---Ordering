package com.example.food_order.data.model.common

import com.google.gson.annotations.SerializedName

data class AddressResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("message")
    val message: String
)