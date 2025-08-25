package com.example.food_order.data.model.common

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("id")
    val id: String?,
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("addressLine1")
    val addressLine1: String,
    @SerializedName("addressLine2")
    val addressLine2: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("zipCode")
    val zipCode: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?
)