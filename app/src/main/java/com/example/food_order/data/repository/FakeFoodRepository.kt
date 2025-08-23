package com.example.food_order.data.repository

import com.example.food_order.data.model.common.Category
import com.example.food_order.data.model.common.FoodItem
import com.example.food_order.data.model.common.Restaurant
import java.time.Instant
import java.util.UUID

object FakeFoodRepository {

    fun getFeaturedRestaurants(): List<Restaurant> {
        return listOf(
            Restaurant(
                id = UUID.randomUUID().toString(),
                ownerId = UUID.randomUUID().toString(),
                name = "Cơm Tấm Phúc Lộc Thọ",
                address = "236 Đ. Đinh Tiên Hoàng, P. Đa Kao, Quận 1, TP.HCM",
                categoryId = "40fbcc64-9ea9-48d7-9a13-486bc74e7288", // ID của category "Cơm sườn"
                latitude = 10.7916,
                longitude = 106.6960,
                createdAt = "20/8/2025",
                distance = 1.2,
                imageUrl = "https://cdn.pastaxi-manager.onepas.vn/content/uploads/articles/lentam/com-tam-phuc-loc-tho/com-tam-phuc-loc-tho-1.jpg"
            ),
            Restaurant(
                id = UUID.randomUUID().toString(),
                ownerId = UUID.randomUUID().toString(),
                name = "Bánh Mì Huynh Hoa",
                address = "26 Đ. Lê Thị Riêng, P. Bến Thành, Quận 1, TP.HCM",
                categoryId = "bd6e9d4f-aa6d-498d-a28f-81650657a104", // ID của category "Bánh mì"
                latitude = 10.7715,
                longitude = 106.6917,
                createdAt = "20/8/2025",
                distance = 2.5,
                imageUrl = "https://cdn.tgdd.vn/Files/2022/01/25/1412805/cach-lam-banh-mi-thit-nuong-thom-ngon-chuan-vi-mien-tay-202201250302494191.jpg"
            ),
            Restaurant(
                id = UUID.randomUUID().toString(),
                ownerId = UUID.randomUUID().toString(),
                name = "Phở Thìn 1955",
                address = "1 P. Hàng Tre, Lý Thái Tổ, Hoàn Kiếm, Hà Nội",
                categoryId = "b3e57c8d-3c23-4e3c-9b57-e4d7fb28cdf6", // ID của category "Bún"
                latitude = 21.0335,
                longitude = 105.8544,
                createdAt = "20/8/2025",
                distance = 0.5,
                imageUrl = "https://i.ex-cdn.com/nhadautu.vn/files/content/2022/09/20/pho-thin-1-1906.jpg"
            ),
            Restaurant(
                id = UUID.randomUUID().toString(),
                ownerId = UUID.randomUUID().toString(),
                name = "Pizza 4P's",
                address = "8/15 Đ. Lê Thánh Tôn, P. Bến Nghé, Quận 1, TP.HCM",
                categoryId = "a71fd846-e105-4a0e-bbc0-89323bf6ea8d", // ID của category "Pizza"
                latitude = 10.7797,
                longitude = 106.7042,
                createdAt = "20/8/2025",
                distance = 3.1,
                imageUrl = "https://images.squarespace-cdn.com/content/v1/580fba05d1758e5a7b527218/1647491684305-61M2G10JJ4330L9G582J/PIZZA-4PS-RESTAURANT-DESIGN-ARCHITECTURE-INTERIOR-01.jpg"
            )
        )
    }

    fun getPopularItems(): List<FoodItem> {
        return listOf(
            FoodItem(
                1,
                "Bún bò Huế",
                55000.0,
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/khoai_tay_chien_hudenv.jpg"
            ),
            FoodItem(
                2,
                "Bánh xèo miền Tây",
                45000.0,
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/khoai_tay_chien_hudenv.jpg"
            ),
            FoodItem(
                3,
                "Gỏi cuốn tôm thịt",
                30000.0,
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/khoai_tay_chien_hudenv.jpg"
            ),
            FoodItem(
                4,
                "Nem lụi",
                40000.0,
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/khoai_tay_chien_hudenv.jpg"
            ),
            FoodItem(
                5,
                "Chè ba màu",
                25000.0,
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/khoai_tay_chien_hudenv.jpg"
            )
        )
    }
}