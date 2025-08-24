package com.example.food_order.ui.owner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_order.data.repository.OrderListSource.OrderSimple
import com.example.food_order.databinding.ListItemDeliveryBinding

class ListDeliveryAdapter(
    private val data: MutableList<OrderSimple>,
    private val onClick: (OrderSimple) -> Unit = {}
) : RecyclerView.Adapter<ListDeliveryAdapter.VH>() {

    inner class VH(val binding: ListItemDeliveryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ListItemDeliveryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = with(holder.binding) {
        val order = data[position]

        tvStatus.text = order.status.name
        tvOrderId.text = "Đơn #${order.id}"
        tvCustomer.text = order.customer
        tvAddress.text = "Địa chỉ: ${order.address}"
        tvNote.text = "Ghi chú: ${order.note}"
        tvTotal.text = "${order.total}S"
        tvTime.text = order.timeText

        rcvItemsRight.apply {
            layoutManager = LinearLayoutManager(root.context)
            adapter = OrderLineItemAdapter(order.items)
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
        }

        root.setOnClickListener { onClick(order) }
    }

    override fun getItemCount() = data.size

    fun submitList(newList: List<OrderSimple>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }
}
