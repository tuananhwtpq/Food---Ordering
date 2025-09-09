package com.example.food_order.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_order.R
import com.example.food_order.data.model.common.Order
import com.example.food_order.databinding.ItemOrderHistoryBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class OrderHistoryAdapter(
    private val onItemClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>() {

    private var orderList = mutableListOf<Order>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding =
            ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    fun setData(newOrders: List<Order>) {
        this.orderList.clear()
        this.orderList.addAll(newOrders)
        notifyDataSetChanged()
    }

    inner class OrderViewHolder(private val binding: ItemOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.tvRestaurantName.text = order.restaurant?.name ?: "Không rõ nhà hàng"
            binding.tvOrderStatus.text = order.status

            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
            val formatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
            try {
                val date = parser.parse(order.createdAt)
                binding.tvOrderDate.text = formatter.format(date)
            } catch (e: Exception) {
                binding.tvOrderDate.text = order.createdAt.take(10)
            }

            val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            binding.tvOrderTotal.text = "Tổng: ${currencyFormatter.format(order.totalAmount)}"

            binding.tvOrderItems.text =
                order.items.joinToString { "${it.menuItemName} x${it.quantity}" }

            Glide.with(itemView.context)
                .load(order.restaurant?.imageUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(binding.imgRestaurant)

            itemView.setOnClickListener {
                onItemClick(order)
            }
        }
    }
}