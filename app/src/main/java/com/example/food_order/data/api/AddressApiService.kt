package com.example.food_order.data.api

import com.example.food_order.data.model.common.Address
import com.example.food_order.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.GET

interface AddressApiService {
    @GET("addresses")
    suspend fun getAddresses(): Response<BaseResponse<List<Address>>>
}