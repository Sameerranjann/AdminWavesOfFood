package com.example.adminwavesoffood.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminwavesoffood.databinding.DeliveryItemBinding

class DeliveryAdapter (private val customerNames: MutableList<String>, private val moneyStatus: MutableList<Boolean>): RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
       val binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DeliveryViewHolder(binding)
    }



    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
      holder.bind(position)
    }
    override fun getItemCount(): Int =customerNames.size
    inner class DeliveryViewHolder (private val binding :DeliveryItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                if (moneyStatus[position] == true){
                    notReceived.text   = "Received"
                }else{
                    notReceived.text = "NotReceived"
                }
                val colorMap = mapOf(
                    true to Color.GREEN, false to Color.RED
                )
                notReceived.setTextColor(colorMap[moneyStatus[position]]?:Color.BLACK)
                statusColor.backgroundTintList = ColorStateList.valueOf(colorMap[moneyStatus[position]]?:Color.BLACK)
            }
        }

    }
}