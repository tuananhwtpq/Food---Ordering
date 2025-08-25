package com.example_food_order.data.repository

/**
 * Chỉ giữ model UI dùng cho adapter. Không để fake-data/repository ở đây nữa.
 */
import android.annotation.SuppressLint
import com.example.food_order.data.repository.FakeMenuRepository.memory
import java.text.SimpleDateFormat
import kotlin.math.absoluteValue
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


// ========================== DATA CLASS ==========================


data class MenuItem(
    val id: String? = null,
    val restaurantId: String,
    val name: String,
    val description: String? = null,
    val price: Double,
    val imageUrl: String? = null,
    val arModelUrl: String? = null,
    val createdAt: String? = null
)

// ========================== REPOSITORY API ==========================
interface MenuRepository {
    suspend fun getMenu(restaurantId: String): List<MenuItem>
    suspend fun update(item: MenuItem): MenuItem              // update 1 món (id phải có khi update lên BE)
    suspend fun delete(id: String)                            // xoá theo id
}

// ========================== FAKE REPOSITORY ==========================
object FakeMenuRepository : MenuRepository {
    // dữ liệu mẫu gắn trong bộ nhớ
    private val memory = mutableListOf(
        MenuItem(
            id = "1",
            restaurantId = "r1",
            name = "Bún chả",
            description = "Thịt nướng + bún, rau sống",
            price = 45000.0,
            imageUrl = "https://picsum.photos/seed/buncha/400/300",
            createdAt = nowIso()
        ),
        MenuItem(
            id = "2",
            restaurantId = "r1",
            name = "Phở bò",
            description = "Bò tái nạm",
            price = 50000.0,
            imageUrl = "https://picsum.photos/seed/phobo/400/300",
            createdAt = nowIso(-1)
        ),
        MenuItem(
            id = "3",
            restaurantId = "r1",
            name = "Bánh mì pate",
            description = null,
            price = 20000.0,
            imageUrl = "https://picsum.photos/seed/banhmi/400/300",
            createdAt = nowIso(-2)
        )
    )
   suspend fun newItemMenu(item: MenuItem): MenuItem {
        memory.add(memory.size+1, item)

        return item
    }

    override suspend fun getMenu(restaurantId: String): List<MenuItem> =
        memory.filter { it.restaurantId == restaurantId }


    override suspend fun update(item: MenuItem): MenuItem {
        val idx = item.id?.let { id -> memory.indexOfFirst { it.id == id } } ?: -1
        return if (idx >= 0) {
            // cập nhật hiện có (không đổi id, restaurantId, createdAt nếu null)
            val old = memory[idx]
            val updated = old.copy(
                name = item.name,
                description = item.description,
                price = item.price,
                imageUrl = item.imageUrl,
                arModelUrl = item.arModelUrl,
                createdAt = item.createdAt ?: old.createdAt
            )
            memory[idx] = updated
            updated
        } else {
            // "thêm mới" (nếu id null) để tiện demo; BE thật có thể là POST
            val newId = ((memory.maxOfOrNull { it.id?.toIntOrNull() ?: 0 } ?: 0) + 1).toString()
            val inserted = item.copy(
                id = newId,
                createdAt = item.createdAt ?: nowIso()
            )
            memory.add(inserted)
            inserted
        }
    }

    override suspend fun delete(id: String) {
        memory.removeAll { it.id == id }
    }

    private fun nowIso(offsetDays: Int = 0): String {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.add(Calendar.DAY_OF_YEAR, offsetDays)
        val sdf = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            Locale.ROOT
        ) // ví dụ: 2025-08-20T09:30:00+00:00
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(cal.time)
    }


    // ========================== RETROFIT API (THẬT) ==========================
// Điều chỉnh path cho khớp BE của bạn:
    interface MenuApi {
        // ví dụ: GET /restaurants/{restaurantId}/menu -> List<MenuItem>
        @retrofit2.http.GET("/restaurants/{restaurantId}/menu")
        suspend fun getMenu(@retrofit2.http.Path("restaurantId") restaurantId: String): List<MenuItem>

        // ví dụ: PUT /menu-items/{id} -> MenuItem
        @retrofit2.http.PUT("/menu-items/{id}")
        suspend fun update(
            @retrofit2.http.Path("id") id: String,
            @retrofit2.http.Body body: MenuItemUpdateBody
        ): MenuItem

        // ví dụ: DELETE /menu-items/{id}
        @retrofit2.http.DELETE("/menu-items/{id}")
        suspend fun delete(@retrofit2.http.Path("id") id: String)
    }

    data class MenuItemUpdateBody(
        val name: String,
        val description: String?,
        val price: Double,
        val imageUrl: String?
    )

    // Triển khai repository dùng API thật
    class ApiMenuRepository(
        private val api: MenuApi
    ) : MenuRepository {
        override suspend fun getMenu(restaurantId: String): List<MenuItem> =
            api.getMenu(restaurantId)

        override suspend fun update(item: MenuItem): MenuItem {
            val id = item.id ?: error("Update cần có id (MenuItem.id == null)")
            val body = MenuItemUpdateBody(
                name = item.name,
                description = item.description,
                price = item.price,
                imageUrl = item.imageUrl
            )
            return api.update(id, body)
        }

        override suspend fun delete(id: String) = api.delete(id)
    }

    // ========================== PROVIDER (ĐỔI NGUỒN 1 DÒNG) ==========================
    object MenuRepositoryProvider {
        @Volatile
        private var repo: MenuRepository = FakeMenuRepository

        fun instance(): MenuRepository = repo

        /** Chuyển sang dữ liệu giả (offline) */
        fun useFake() {
            repo = FakeMenuRepository
        }

        /** Chuyển sang dùng API thật */
        fun useApi(api: MenuApi) {
            repo = ApiMenuRepository(api)
        }
    }
}
