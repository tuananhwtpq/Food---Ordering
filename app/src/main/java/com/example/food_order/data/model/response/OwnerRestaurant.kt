package com.example.food_order.data.model.response

// Tối giản đúng thứ FE cần để hiển thị chọn nhanh.
// BE trả thêm field nào cũng không sao vì Gson sẽ bỏ qua.
data class OwnerRestaurantsResponse(
    val data: List<OwnerRestaurant>
)

data class OwnerRestaurant(
    val id: String,
    val ownerId: String,
    val name: String,
    val address: String?,
    val imageUrl: String?
)
