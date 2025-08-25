package com.example.food_order.data.repository

import com.example.food_order.data.api.AddressApiService
import com.example.food_order.data.model.common.Address
import com.example.food_order.utils.extension.parseError

class AddressRepository(
    private val apiService: AddressApiService
) {
    suspend fun getAddresses(): Result<List<Address>> {
        return try {
            val response = apiService.getAddresses()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.parseError() ?: "Lỗi lấy danh sách địa chỉ"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}