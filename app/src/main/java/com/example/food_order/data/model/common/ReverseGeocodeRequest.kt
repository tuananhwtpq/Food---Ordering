package com.example.food_order.data.model.common

import com.google.gson.annotations.SerializedName

data class ReverseGeocodeRequest(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)