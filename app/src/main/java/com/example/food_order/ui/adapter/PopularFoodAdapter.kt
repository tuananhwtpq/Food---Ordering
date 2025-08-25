package com.example.food_order.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_order.data.model.common.Category
import com.example.food_order.data.model.common.FoodItem
import com.example.food_order.data.model.common.Restaurant
import com.example.food_order.databinding.ItemFoodPopularBinding
import java.text.NumberFormat
import java.util.Locale

class PopularFoodAdapter(
    private val onItemClick: (FoodItem) -> Unit
) :
    RecyclerView.Adapter<PopularFoodAdapter.PopularFoodViewHolder>() {

    private var foodItems: List<FoodItem> = emptyList()

    inner class PopularFoodViewHolder(private val binding: ItemFoodPopularBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(foodItem: FoodItem) {
            binding.tvFoodName.text = foodItem.name
            val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            binding.tvFoodPrice.text = formatter.format(foodItem.price)
            Glide.with(binding.root.context)
                .load(foodItem.imageUrl)
                .into(binding.ivFoodImage)

            binding.root.setOnClickListener {
                onItemClick(foodItem)
            }
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

    fun setData(newFoodItems: List<FoodItem>) {
        this.foodItems = newFoodItems
        notifyDataSetChanged()
    }
}