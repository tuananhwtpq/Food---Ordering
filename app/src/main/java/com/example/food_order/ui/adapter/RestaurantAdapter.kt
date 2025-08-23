package com.example.food_order.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_order.data.model.common.Category
import com.example.food_order.data.model.common.Restaurant
import com.example.food_order.databinding.ItemRestaurantBinding

class RestaurantAdapter(
    private val onItemClick: (Restaurant) -> Unit
) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    private var restaurants: List<Restaurant> = emptyList()

    inner class RestaurantViewHolder(private val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant) {
            binding.tvRestaurantName.text = restaurant.name
            binding.tvRestaurantRating.text = "⭐"
            Glide.with(binding.root.context)
                .load(restaurant.imageUrl)
                .into(binding.ivRestaurantImage)

            binding.root.setOnClickListener {
                onItemClick(restaurant)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding =
            ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(restaurants[position])
    }

    override fun getItemCount(): Int = restaurants.size

    fun setData(newRestaunrants: List<Restaurant>) {
        this.restaurants = newRestaunrants
        notifyDataSetChanged()
    }
}