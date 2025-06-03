package com.example.adminwavesoffood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwavesoffood.adapter.DeliveryAdapter
import com.example.adminwavesoffood.databinding.ActivityOutForDeliveryBinding
import com.example.adminwavesoffood.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding : ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private  var listOfCompleteOrderList : ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backButton.setOnClickListener{
            finish()
        }
        //reterive and display completed order
        retriveCompleteOrderDetail()


    }

    private fun retriveCompleteOrderDetail() {
       database = FirebaseDatabase.getInstance()
        val completeOrderReferencem = database.reference.child("CompletedOrder")
            .orderByChild("currentItem")
        completeOrderReferencem.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompleteOrderList.clear()


                for (orderSnapshot in snapshot.children){
                  val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it)
                    }
                }
                listOfCompleteOrderList.reversed()

                setDataIntoRecyclerview()

            }



            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun setDataIntoRecyclerview() {
        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()
        for (order in listOfCompleteOrderList){
            order.userName?.let {
                customerName.add(it)
            }
            moneyStatus.add(order.paymentReceived)
        }
        val adapter = DeliveryAdapter(customerName,moneyStatus)
        binding.deliveryRecyclerView.adapter = adapter
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}