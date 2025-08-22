package com.example.food_order.ui.owner.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.food_order.data.repository.OrderListSource.OrderSimple
import com.example.food_order.databinding.ItemOrderBinding
import com.example.food_order.ui.owner.adapter.OrderLineItemAdapter

class OwnerOrderAdapter(
    private val onAccept: (OrderSimple) -> Unit,
    private val onReject: (OrderSimple) -> Unit
) : RecyclerView.Adapter<OwnerOrderAdapter.VH>() {

    private var data: List<OrderSimple> = emptyList()

    inner class VH(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderSimple) {
            binding.tvOrderId.text = "Đơn #${item.id}"
            binding.tvStatus.text = item.status.name
            binding.tvCustomer.text = item.customer ?: "(không có tên)"
            binding.tvAddress.text = "Địa chỉ: ${item.address.orEmpty()}"
            binding.tvNote.text = if (item.note.isNullOrBlank()) "Ghi chú: -" else "Ghi chú: ${item.note}"
            binding.tvTotal.text = "₫${item.total}"
            binding.tvTime.text = item.timeText

            // nested RecyclerView cho danh sách món trong đơn
            binding.rcvItemsRight.adapter = OrderLineItemAdapter(item.items)

            binding.btnAccept.setOnClickListener { onAccept(item) }
            binding.btnReject.setOnClickListener { onReject(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(data[position])
    override fun getItemCount(): Int = data.size

    fun submitList(newList: List<OrderSimple>) {
        data = newList
        notifyDataSetChanged()
    }
}
