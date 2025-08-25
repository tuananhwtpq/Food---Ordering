package com.example.food_order.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.food_order.data.api.OrdersApiService
import com.example.food_order.data.api.OrdersListResponse
import com.example.food_order.data.model.response.OrderResponse
import com.example.food_order.di.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.time.*
import java.time.format.DateTimeFormatter
import kotlin.math.abs

data class OwnerDailyStats(
    val dateIso: String,      // YYYY-MM-DD (hôm nay)
    val totalRevenue: Double,
    val totalOrders: Int,
    val pendingOrders: Int
)

class StatsRepository private constructor(
    private val ordersApi: OrdersApiService
) {

    companion object {
        fun from(context: Context): StatsRepository {
            val s = RetrofitInstance.createAuthorizedServiceGeneric<OrdersApiService>(context)
            return StatsRepository(s)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loadToday(): Result<OwnerDailyStats> = safeIO {
        val res = ordersApi.getOwnerOrders()
        if (!res.isSuccessful) throw HttpException(res)
        val body = res.body() ?: OrdersListResponse(emptyList())
        val orders = body.orders

        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)

        // Cố gắng lọc theo "hôm nay" nếu parse được createdAt; nếu không → dùng toàn bộ
        val parsed = orders.mapNotNull { o -> o to parseInstantOrNull(o.createdAt, zone) }
        val useFiltered = parsed.isNotEmpty()

        val todayOrders: List<OrderResponse> = if (useFiltered) {
            parsed.filter { (_, inst) -> inst?.atZone(zone)!!.toLocalDate() == today }.map { it.first }
        } else orders

        val totalRevenue = todayOrders.sumOf { it.totalAmount }
        val totalOrders = todayOrders.size
        val pendingOrders = todayOrders.count { it.status.equals("PENDING_ACCEPTANCE", true) }

        OwnerDailyStats(
            dateIso = today.toString(),
            totalRevenue = totalRevenue,
            totalOrders = totalOrders,
            pendingOrders = pendingOrders
        )
    }

    private suspend inline fun <T> safeIO(crossinline block: suspend () -> T): Result<T> =
        withContext(Dispatchers.IO) {
            try { Result.success(block()) } catch (e: Exception) { Result.failure(e) }
        }

    /** Parse mềm nhiều định dạng createdAt khác nhau. */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseInstantOrNull(s: String?, zone: ZoneId): Instant? {
        if (s.isNullOrBlank()) return null

        // 1) ISO_INSTANT: 2025-08-24T06:12:33Z
        runCatching { return Instant.parse(s) }

        // 2) ISO_OFFSET_DATE_TIME: 2025-08-24T13:12:33+07:00
        runCatching { return OffsetDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant() }

        // 3) ISO_LOCAL_DATE_TIME (giả sử server time là UTC hoặc local server)
        runCatching { return LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(ZoneOffset.UTC).toInstant() }

        // 4) Epoch (ms/s) dạng chuỗi số
        runCatching {
            val num = s.toLong()
            return if (abs(num) > 10_000_000_000L) Instant.ofEpochMilli(num) else Instant.ofEpochSecond(num)
        }

        return null
    }
}