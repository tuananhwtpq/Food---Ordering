package com.example.food_order.ui.owner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_order.R
import com.example.food_order.databinding.MenuItemBinding
import com.example.food_order.data.repository.MenuItem
import java.text.NumberFormat
import java.util.Locale


class MenuAdapter(
    private var items: List<MenuItem> = emptyList(),
    private val onItemClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MenuItem) = with(binding) {
            tvName.text = item.name

            // Mô tả ẩn nếu trống
            if (item.description.isNullOrBlank()) {
                tvDesc.visibility = View.GONE
            } else {
                tvDesc.visibility = View.VISIBLE
                tvDesc.text = item.description
            }

            // Giá VNĐ
            val us = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            tvPrice.text = us.format(item.price)

            // Ảnh
            Glide.with(root.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_image_placeholder) // đổi theo project nếu có
                .error(R.drawable.ic_broken_image)            // đổi theo project nếu có
                .into(ivImage)

            root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submit(newItems: List<MenuItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
