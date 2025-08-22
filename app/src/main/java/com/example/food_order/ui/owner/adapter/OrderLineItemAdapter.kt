package com.example.food_order.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.food_order.data.repository.OrderListSource.OrderItem
import com.example.food_order.databinding.CustomerListOrderBinding

class OrderLineItemAdapter(private val items: List<OrderItem>) :
    RecyclerView.Adapter<OrderLineItemAdapter.VH>() {

    inner class VH(val binding: CustomerListOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = CustomerListOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.tvOrderName.text = item.name
        holder.binding.tvOrderQuantify.text = "x${item.quantity}"
    }

    override fun getItemCount() = items.size
}
