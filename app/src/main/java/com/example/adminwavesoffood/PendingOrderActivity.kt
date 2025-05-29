package com.example.adminwavesoffood

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwavesoffood.adapter.DeliveryAdapter
import com.example.adminwavesoffood.adapter.PendingOrderAdapter
import com.example.adminwavesoffood.databinding.ActivityPendingOrderBinding

class PendingOrderActivity : AppCompatActivity() {
    private val binding : ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            finish()
        }

        val orderedCustomerName = arrayListOf(
            "Ram",
            "Raj","Raju"

        )
        val orderQuantity = arrayListOf(
            "8","6","5"
        )
        val orderedFoodImage = arrayListOf(R.drawable.foodmenu,R.drawable.foodmenu,R.drawable.foodmenu)
        val adapter = PendingOrderAdapter(orderedCustomerName, orderQuantity, orderedFoodImage, this)

        binding.pendingOrderRecyclerView.adapter = adapter
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    }
