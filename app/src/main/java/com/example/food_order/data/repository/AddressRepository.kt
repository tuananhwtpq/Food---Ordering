package com.example.food_order.data.repository

import com.example.food_order.data.api.AddressApiService
import com.example.food_order.data.model.common.Address
import com.example.food_order.data.model.common.ReverseGeocodeRequest
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

    suspend fun addAddress(address: Address): Result<Unit> {
        return try {
            val response = apiService.addAddress(address)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.parseError() ?: "Lỗi thêm địa chỉ"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAddress(addressId: String, address: Address): Result<Unit> {
        return try {
            val response = apiService.updateAddress(addressId, address)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.parseError() ?: "Lỗi cập nhật địa chỉ"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAddress(addressId: String): Result<Unit> {
        return try {
            val response = apiService.deleteAddress(addressId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.parseError() ?: "Lỗi xóa địa chỉ"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun reverseGeocode(latitude: Double, longitude: Double): Result<Address> {
        return try {
            val request = ReverseGeocodeRequest(latitude, longitude)
            val response = apiService.reverseGeocode(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.parseError() ?: "Lỗi dịch ngược tọa độ"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}