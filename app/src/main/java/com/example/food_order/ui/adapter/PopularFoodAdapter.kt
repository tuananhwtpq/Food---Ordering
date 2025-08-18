package com.example.food_order.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_order.data.model.common.FoodItem
import com.example.food_order.databinding.ItemFoodPopularBinding
import java.text.NumberFormat
import java.util.Locale

class PopularFoodAdapter(private val foodItems: List<FoodItem>) :
    RecyclerView.Adapter<PopularFoodAdapter.PopularFoodViewHolder>() {

    inner class PopularFoodViewHolder(private val binding: ItemFoodPopularBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(foodItem: FoodItem) {
            binding.tvFoodName.text = foodItem.name
            // Format giá tiền sang định dạng VNĐ
            val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            binding.tvFoodPrice.text = formatter.format(foodItem.price)
            Glide.with(binding.root.context)
                .load(foodItem.imageUrl)
                .into(binding.ivFoodImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularFoodViewHolder {
        val binding =
            ItemFoodPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularFoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularFoodViewHolder, position: Int) {
        holder.bind(foodItems[position])
    }

    override fun getItemCount(): Int = foodItems.size
}