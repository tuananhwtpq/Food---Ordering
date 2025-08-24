package com.example.food_order.ui.stats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_order.data.repository.StatsRepository
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class OwnerStatsViewModel(
    private val repo: StatsRepository
) : ViewModel() {

    data class Ui(
        val dateText: String = "",
        val revenueText: String = "0",
        val totalOrdersText: String = "0",
        val pendingOrdersText: String = "0"
    )

    private val _ui = MutableLiveData(Ui())
    val ui: LiveData<Ui> = _ui

    /** Gọi khi Home hiển thị để load thống kê hôm nay */
    @RequiresApi(Build.VERSION_CODES.O)
    fun refresh() {
        viewModelScope.launch {
            repo.loadToday().onSuccess { s ->
                _ui.value = Ui(
                    dateText = formatDateVi(s.dateIso),             // "dd/MM/yyyy"
                    revenueText = formatVnd(s.totalRevenue),        // "1.234.567 ₫"
                    totalOrdersText = s.totalOrders.toString(),
                    pendingOrdersText = s.pendingOrders.toString()
                )
            }
        }
    }

    private fun formatVnd(v: Double): String =
        NumberFormat.getCurrencyInstance(Locale("vi", "US")).format(v)

    private fun formatDateVi(iso: String): String {
        // "YYYY-MM-DD" -> "DD/MM/YYYY"
        val p = iso.split('-')
        return if (p.size == 3) "${p[2]}/${p[1]}/${p[0]}" else iso
    }
}
