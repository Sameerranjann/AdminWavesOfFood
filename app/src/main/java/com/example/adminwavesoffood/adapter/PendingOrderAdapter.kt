package com.example.adminwavesoffood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwavesoffood.databinding.PendingOrdersItemBinding

class PendingOrderAdapter(
    private val context: Context,
    private val customerNames: MutableList<String>,
    private val quantity: MutableList<String>,
    private val foddImage: MutableList<String>,
    private val itemClicked : OnItemClicked
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {
 interface  OnItemClicked{
     fun onItemClickListener(position: Int)
     fun onItemAcceptClickListener(position: Int)
     fun onItemDispatchClickListener(position: Int)
 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding =
            PendingOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }



    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size
    inner class PendingOrderViewHolder(private val binding: PendingOrdersItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            if (position >= customerNames.size || position >= quantity.size || position >= foddImage.size) {
                return // Prevent IndexOutOfBoundsException
            }
            binding.apply {
                customerNamee.text = customerNames[position]
                pendingOrderQuantity.text = quantity[position]
                var uriString = foddImage[position]
                var uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(foodImage)

                orderAcceptButton.apply {
                    setOnClickListener {
                        if (isAccepted) {
                            text = "Accept"
                        }
                        else {
                            text = "Dispatch"

                        }
                        setOnClickListener {
                            if (!isAccepted) {
                                text = "Dispatch"
                                isAccepted = true
                              showToast("Order is accepted")
                                itemClicked.onItemAcceptClickListener(position
                                )
                            }
                            else{
                               customerNames.removeAt(adapterPosition)
                                quantity.removeAt(adapterPosition)
                                foddImage.removeAt(adapterPosition)
                                notifyItemRemoved(adapterPosition)
                                notifyItemRemoved(adapterPosition)
                                showToast("Order is dispatched")
                                itemClicked.onItemDispatchClickListener(position)
                            }

                        }

                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClickListener(position)
                }

            }

        }
         private fun showToast(message:String){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }

    }
}
