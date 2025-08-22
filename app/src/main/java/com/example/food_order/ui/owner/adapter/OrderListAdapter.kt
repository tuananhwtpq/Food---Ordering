package com.example.food_order.ui.owner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_order.data.repository.OrderListSource
import com.example.food_order.databinding.CustomerListOrderBinding
import com.example.food_order.databinding.ItemOrderBinding
import com.example.food_order.ui.owner.adapter.OrderLineItemAdapter
import java.text.NumberFormat
import java.util.Locale

class OrderListAdapter(
    private var orders: List<OrderListSource.OrderSimple>,
    private val onAccept: (OrderListSource.OrderSimple) -> Unit,
    private val onReject: (OrderListSource.OrderSimple) -> Unit
) : RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>() {

    // Tối ưu cho nested RecyclerView
    private val sharedPool = RecyclerView.RecycledViewPool()

    inner class OrderViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: OrderListSource.OrderSimple) = with(binding) {
            // ====== Bind thông tin đơn ======
            tvOrderId.text = "Đơn #${order.id?.take(6) ?: "-"}"
            tvStatus.text  = order.status.toString()
            tvCustomer.text = "Khách: ${order.customer ?: "-"}"
            tvAddress.text  = "Địa chỉ: ${order.address ?: "-"}"
            tvNote.text     = if (order.note.isNullOrBlank()) "Ghi chú: -" else "Ghi chú: ${order.note}"

            val vnd = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            tvTotal.text = vnd.format(order.total ?: 0L)
            tvTime.text  = order.timeText ?: "" // hoặc format lại thời gian nếu cần

            // ====== RecyclerView con: danh sách món ======
            rcvItemsRight.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = OrderLineItemAdapter(order.items ?: emptyList())
                setRecycledViewPool(sharedPool)
                isNestedScrollingEnabled = false
            }

            // ====== Nút hành động ======
            btnAccept.setOnClickListener { onAccept(order) }
            btnReject.setOnClickListener { onReject(order) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun submitList(newOrders: List<OrderListSource.OrderSimple>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}

/**
 * Adapter hiển thị các món trong 1 đơn (RecyclerView con).
 * Dùng layout: customer_list_order.xml (CustomerListOrderBinding)
 * Cần các field: name, quantity trên OrderListSource.OrderItemSimple
 */

