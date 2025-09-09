package com.example.food_order.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_order.R
import com.example.food_order.data.repository.MenuItem
import com.example.food_order.databinding.ItemMenuFoodBinding

class MenuAdapter(
    private val onItemClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private var menuList: List<MenuItem> = emptyList()

    fun setData(newList: List<MenuItem>) {
        this.menuList = newList
        notifyDataSetChanged()
    }

    inner class MenuViewHolder(private val binding: ItemMenuFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menuItem: MenuItem) {
            binding.tvFoodName.text = menuItem.name
            binding.tvFoodDescription.text = menuItem.description // Giả sử có trường description
            binding.tvFoodPrice.text = "${menuItem.price}đ" // Định dạng giá

            Glide.with(binding.root.context)
                .load(menuItem.imageUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(binding.ivFoodImage)

            binding.root.setOnClickListener {
                onItemClick(menuItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuFoodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menuList[position])
    }

    override fun getItemCount(): Int = menuList.size
}