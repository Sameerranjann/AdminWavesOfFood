package com.example.adminwavesoffood.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwavesoffood.AllItemActivity
import com.example.adminwavesoffood.databinding.ItemItemBinding
import com.example.adminwavesoffood.model.AllMenu
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(
    @SuppressLint("RestrictedApi") private val context: AllItemActivity,
    private val menuList: ArrayList<AllMenu>,
    private val databaseReference: DatabaseReference
) : RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {

    private val itemQuantities = IntArray(menuList.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size

    inner class AddItemViewHolder(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val menuItem = menuList[position]
            val quantity = itemQuantities[position]

            binding.apply {
                fooddNameTextView.text = menuItem.name
                pendingOrderQuantity.text = menuItem.price
                quantityTextView.text = quantity.toString()

                // ✅ Safe image loading
                if (!menuItem.imageUrl.isNullOrEmpty()) {
                    Glide.with(root.context)
                        .load(menuItem.imageUrl)
                        .into(foodImageView)
                }

                // 🔘 Button actions
                minusButton.setOnClickListener { decreaseQuantity(position) }
                plusButton.setOnClickListener { increaseQuantity(position) }
                deleteButton.setOnClickListener { deleteItem(position) }
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                binding.quantityTextView.text = itemQuantities[position].toString()
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                binding.quantityTextView.text = itemQuantities[position].toString()
            }
        }

        private fun deleteItem(position: Int) {
            if (position in menuList.indices) {
                menuList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, menuList.size)
            }
        }
    }
}
