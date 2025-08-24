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
                name = "Muối Quán",
                address = "236 Đ. Đinh Tiên Hoàng, P. Đa Kao, Quận 1, TP.HCM",
                categoryId = "40fbcc64-9ea9-48d7-9a13-486bc74e7288",
                latitude = 10.7916,
                longitude = 106.6960,
                createdAt = "20/8/2025",
                distance = 1.2,
                imageUrl = "https://res.cloudinary.com/dsizusoj0/image/upload/v1755966975/40-mau-bien-quang-cao-nha-hang-quan-an-tao-hieu-ung-tot-nhat-25_wfge3w.gif"
            ),
            Restaurant(
                id = UUID.randomUUID().toString(),
                ownerId = UUID.randomUUID().toString(),
                name = "Góc ăn vặt Trần Phú",
                address = "26 Đ. Lê Thị Riêng, P. Bến Thành, Quận 1, TP.HCM",
                categoryId = "bd6e9d4f-aa6d-498d-a28f-81650657a104", // ID của category "Bánh mì"
                latitude = 10.7715,
                longitude = 106.6917,
                createdAt = "20/8/2025",
                distance = 2.5,
                imageUrl = "https://res.cloudinary.com/dsizusoj0/image/upload/v1755967310/bien-quang-cao-quan-an-vat-1_x6gury.jpg"
            ),
            Restaurant(
                id = UUID.randomUUID().toString(),
                ownerId = UUID.randomUUID().toString(),
                name = "Cơm tấm Phúc Lộc Thọ",
                address = "1 P. Hàng Tre, Lý Thái Tổ, Hoàn Kiếm, Hà Nội",
                categoryId = "b3e57c8d-3c23-4e3c-9b57-e4d7fb28cdf6", // ID của category "Bún"
                latitude = 21.0335,
                longitude = 105.8544,
                createdAt = "20/8/2025",
                distance = 0.5,
                imageUrl = "https://res.cloudinary.com/dsizusoj0/image/upload/v1755966976/R_i3rcq0.jpg"
            ),
            Restaurant(
                id = UUID.randomUUID().toString(),
                ownerId = UUID.randomUUID().toString(),
                name = "Ăn vặt Ngọc Ánh",
                address = "8/15 Đ. Lê Thánh Tôn, P. Bến Nghé, Quận 1, TP.HCM",
                categoryId = "a71fd846-e105-4a0e-bbc0-89323bf6ea8d", // ID của category "Pizza"
                latitude = 10.7797,
                longitude = 106.7042,
                createdAt = "20/8/2025",
                distance = 3.1,
                imageUrl = "https://res.cloudinary.com/dsizusoj0/image/upload/v1755966976/bien-quang-cao-do-an-vat-ngoc-anh-6_kqucdz.jpg"
            ),
            Restaurant(
                id = UUID.randomUUID().toString(),
                ownerId = UUID.randomUUID().toString(),
                name = "Phở hùng",
                address = "8/15 Đ. Lê Phúc Thiện, P. Bến Nghé, Quận 3, TP.HCM",
                categoryId = "a71fd846-e105-4a0e-bbc0-89323bf6ea8d", // ID của category "Pizza"
                latitude = 10.7797,
                longitude = 106.7042,
                createdAt = "20/8/2025",
                distance = 3.1,
                imageUrl = "https://res.cloudinary.com/dsizusoj0/image/upload/v1755966976/pho-hung-vietnamese-beef_rotated_90_wjlpoq.jpg"
            )

        )
    }

    fun getPopularItems(): List<FoodItem> {
        return listOf(
            FoodItem(
                "1",
                "Bún bò Huế",
                "Ngon tuyệt vời",
                55000.0,
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/khoai_tay_chien_hudenv.jpg"
            ),
            FoodItem(
                "2",
                "Bánh xèo miền Tây",
                "Ngon tuyệt vời",
                45000.0,
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/khoai_tay_chien_hudenv.jpg"
            ),
            FoodItem(
                "3",
                "Gỏi cuốn tôm thịt",
                "Ngon tuyệt vời",
                30000.0,
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/khoai_tay_chien_hudenv.jpg"
            ),
            FoodItem(
                "4",
                "Nem lụi",
                "Ngon tuyệt vời",
                40000.0,
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/khoai_tay_chien_hudenv.jpg"
            ),
            FoodItem(
                "5",
                "Chè ba màu",
                "Ngon tuyệt vời",
                25000.0,
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/khoai_tay_chien_hudenv.jpg"
            )
        )
    }
}