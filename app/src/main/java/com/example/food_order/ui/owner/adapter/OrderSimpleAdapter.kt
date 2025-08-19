package com.example.food_order.ui.owner.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.food_order.R
import java.text.NumberFormat
import java.util.Locale

data class FoodQty(val name: String, val qty: Int)
data class OrderSimple(
    val id: String,
    val status: String,              // "PENDING", "ACCEPTED", ...
    val customer: String?,           // có thể null
    val address: String?,
    val note: String?,
    val total: Long,                 // VND
    val timeText: String,            // "HH:mm" hoặc "dd/MM HH:mm"
    val items: List<FoodQty>
)

class OrderSimpleAdapter(
    private val onAccept: (OrderSimple) -> Unit,
    private val onReject: (OrderSimple) -> Unit
) : RecyclerView.Adapter<OrderSimpleAdapter.VH>() {

    private val data = mutableListOf<OrderSimple>()

    fun submit(list: List<OrderSimple>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun current(): List<OrderSimple> = data

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val tvOrderId: TextView = view.findViewById(R.id.tvOrderId)
        private val tvStatus: TextView  = view.findViewById(R.id.tvStatus)
        private val tvCustomer: TextView = view.findViewById(R.id.tvCustomer)
        private val tvAddress: TextView  = view.findViewById(R.id.tvAddress)
        private val tvNote: TextView     = view.findViewById(R.id.tvNote)
        private val tvTotal: TextView    = view.findViewById(R.id.tvTotal)
        private val tvTime: TextView     = view.findViewById(R.id.tvTime)
        private val tvItemsRight: TextView = view.findViewById(R.id.tvItemsRight)
        private val btnAccept: Button    = view.findViewById(R.id.btnAccept)
        private val btnReject: Button    = view.findViewById(R.id.btnReject)

        fun bind(o: OrderSimple) {
            tvOrderId.text = "Đơn #${o.id.takeLast(6)}"
            tvStatus.text  = o.status
            bindOptional(tvCustomer, o.customer?.let { "Khách: $it" })
            bindOptional(tvAddress,  o.address?.let { "Địa chỉ: $it" })
            bindOptional(tvNote,     o.note?.let { "Ghi chú: $it" })
            tvTotal.text = o.total.toVnd()
            tvTime.text  = o.timeText

            // Bên phải: "SL × Tên món", tối đa 4 dòng cho gọn
            tvItemsRight.text = o.items.take(4).joinToString("\n") { "${it.qty}× ${it.name}" }

            btnAccept.setOnClickListener { onAccept(o) }
            btnReject.setOnClickListener { onReject(o) }
        }

        private fun bindOptional(tv: TextView, value: String?) {
            if (value.isNullOrBlank()) {
                tv.visibility = View.GONE
            } else {
                tv.text = value
                tv.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_simple, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size
}

private fun Long.toVnd(): String =
    NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(this)
