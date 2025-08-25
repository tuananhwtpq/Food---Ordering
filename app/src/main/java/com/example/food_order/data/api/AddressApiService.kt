package com.example.food_order.data.api

import com.example.food_order.data.model.common.Address
import com.example.food_order.data.model.common.AddressResponse
import com.example.food_order.data.model.common.ReverseGeocodeRequest
import com.example.food_order.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AddressApiService {
    @GET("addresses")
    suspend fun getAddresses(): Response<BaseResponse<List<Address>>>

    @POST("addresses")
    suspend fun addAddress(@Body address: Address): Response<AddressResponse>

    @PUT("addresses/{id}")
    suspend fun updateAddress(@Path("id") addressId: String, @Body address: Address): Response<Unit>


    @DELETE("addresses/{id}")
    suspend fun deleteAddress(@Path("id") addressId: String): Response<Unit>

    @POST("addresses/reverse-geocode")
    suspend fun reverseGeocode(@Body request: ReverseGeocodeRequest): Response<Address>
}