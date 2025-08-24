package com.example.food_order.ui.owner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_order.data.repository.OrderListSource.OrderSimple
import com.example.food_order.databinding.ItemOrderBinding

class OwnerOrdersAdapter(
    private val data: MutableList<OrderSimple>,
    private val onAccept: (OrderSimple) -> Unit,
    private val onReject: (OrderSimple) -> Unit
) : RecyclerView.Adapter<OwnerOrdersAdapter.VH>() {

    inner class VH(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = with(holder.binding) {
        val order = data[position]

        tvStatus.text = order.status.name
        tvOrderId.text = "Đơn #${order.id}"
        tvCustomer.text = "Khách: ${order.customer}"
        tvAddress.text = "Địa chỉ: ${order.address}"
        tvNote.text = "Ghi chú: ${order.note}"
        tvTotal.text = "${order.total}$"
        tvTime.text = order.timeText

        // Nested list các món
        rcvItemsRight.apply {
            layoutManager = LinearLayoutManager(root.context)
            adapter = OrderLineItemAdapter(order.items)
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
        }

        btnAccept.setOnClickListener { onAccept(order) }
        btnReject.setOnClickListener { onReject(order) }
    }

    override fun getItemCount() = data.size

    fun submitList(newList: List<OrderSimple>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }
}
