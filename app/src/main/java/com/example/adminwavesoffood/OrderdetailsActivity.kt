package com.example.adminwavesoffood

import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwavesoffood.adapter.OrderDetailsAdapter
import com.example.adminwavesoffood.databinding.ActivityOrderDetailsBinding
import com.example.adminwavesoffood.model.OrderDetails

class OrderdetailsActivity : AppCompatActivity() {

    private val binding : ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    private var userName : String? = null
    private var address : String? = null
    private var phoneNumber : String? = null
    private var totalPrice : String? = null
    private var  foodNames: ArrayList<String> = arrayListOf()
    private var  foodImages: ArrayList<String> = arrayListOf()
    private var  foodQuantity: ArrayList<Int> = arrayListOf()
    private var  foodPrices: ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }
          getDataFromIntent()

    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as? OrderDetails

        if (receivedOrderDetails == null) {
            Log.e("OrderDetailsActivity", "OrderDetails object is null or invalid.")
            return
        }

        userName = receivedOrderDetails.userName
        foodNames = ArrayList(receivedOrderDetails.foodNames ?: emptyList())
        foodImages = ArrayList(receivedOrderDetails.foodImages ?: emptyList())
        foodQuantity = ArrayList(receivedOrderDetails.foodQuantities ?: emptyList())
        address = receivedOrderDetails.address
        phoneNumber = receivedOrderDetails.phoneNumber
        foodPrices = ArrayList(receivedOrderDetails.foodPrices ?: emptyList())
        totalPrice = receivedOrderDetails.totalPrice

        setUserDetails()
        setAdapter()
    }





    private fun setUserDetails() {
        binding.name.text = userName
        binding.address.text = address
        binding.phone.text = phoneNumber
        binding.totalPay.text = totalPrice
    }
    private fun setAdapter() {
          binding.DetailRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this,foodNames,foodImages,foodQuantity,foodPrices)
        binding.DetailRecyclerView.adapter = adapter
    }

}