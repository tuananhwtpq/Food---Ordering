package com.example.food_order.data.repository

import com.example.food_order.data.model.common.Category
import com.example.food_order.data.model.common.FoodItem
import com.example.food_order.data.model.common.Restaurant

object FakeFoodRepository {
    fun getCategories(): List<Category> {
        return listOf(
            Category(
                1,
                "Cơm",
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/banh_my_hr0nlp.jpg"
            ),
            Category(
                2,
                "Phở",
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/banh_my_hr0nlp.jpg"
            ),
            Category(
                3,
                "Bún",
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/banh_my_hr0nlp.jpg"
            ),
            Category(
                4,
                "Bánh mì",
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/banh_my_hr0nlp.jpg"
            ),
            Category(
                5,
                "Ăn vặt",
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/banh_my_hr0nlp.jpg"
            )
        )
    }

    fun getFeaturedRestaurants(): List<Restaurant> {
        return listOf(
            Restaurant(
                1,
                "Cơm Tấm Cali",
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/banh_my_hr0nlp.jpg",
                4.8
            ),
            Restaurant(
                2,
                "Phở Thìn Lò Đúc",
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/banh_my_hr0nlp.jpg",
                4.7
            ),
            Restaurant(
                3,
                "Bún Chả Hương Liên",
                "https://res.cloudinary.com/dsizusoj0/image/upload/v1754997273/banh_my_hr0nlp.jpg",
                4.9
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