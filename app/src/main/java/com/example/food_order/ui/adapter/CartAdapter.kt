package com.example.food_order.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_order.R
import com.example.food_order.data.model.common.CartItem
import com.example.food_order.databinding.ItemCartBinding
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(
    private val onQuantityChanged: (cartItemId: String, newQuantity: Int) -> Unit,
    private val onRemoveItem: (cartItemId: String) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var cartItems = mutableListOf<CartItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartItems[position]
        holder.bind(currentItem)
    }

    fun setData(newCartItems: List<CartItem>) {
        this.cartItems.clear()
        this.cartItems.addAll(newCartItems)
        notifyDataSetChanged()
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            val menuItem = cartItem.menuItem
            binding.tvFoodName.text = menuItem.name
            binding.tvQuantity.text = String.format("%02d", cartItem.quantity)

            val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            binding.tvFoodPrice.text = formatter.format(menuItem.price)

            Glide.with(itemView.context)
                .load(menuItem.imageUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(binding.imgFoodItem)

            binding.btnPlus.setOnClickListener {
                onQuantityChanged(cartItem.id, cartItem.quantity + 1)
            }
            binding.btnMinus.setOnClickListener {
                if (cartItem.quantity > 1) {
                    onQuantityChanged(cartItem.id, cartItem.quantity - 1)
                } else {
                    onRemoveItem(cartItem.id)
                }
            }
            binding.btnDeleteItem.setOnClickListener {
                onRemoveItem(cartItem.id)
            }
        }
    }
}