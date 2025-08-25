package com.example.food_order.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_order.R
import com.example.food_order.data.model.common.Restaurant
import com.example.food_order.databinding.ItemRestaurantListBinding

class RestaurantListAdapter(
    private val onItemClick: (Restaurant) -> Unit
) : RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder>() {

    private var restaurantList: List<Restaurant> = emptyList()

    fun setData(newList: List<Restaurant>) {
        this.restaurantList = newList
        notifyDataSetChanged()
    }

    inner class RestaurantViewHolder(private val binding: ItemRestaurantListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurant: Restaurant) {
            binding.tvRestaurantName.text = restaurant.name
            binding.tvAddress.text = restaurant.address

            binding.tvDistance.text = restaurant.distance.toString() + " km "

            Glide.with(binding.root.context)
                .load(restaurant.imageUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(binding.ivRestaurantImage)

            binding.root.setOnClickListener {
                onItemClick(restaurant)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = ItemRestaurantListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(restaurantList[position])
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }
}