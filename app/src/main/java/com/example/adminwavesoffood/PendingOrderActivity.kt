package com.example.adminwavesoffood

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwavesoffood.adapter.DeliveryAdapter
import com.example.adminwavesoffood.adapter.PendingOrderAdapter
import com.example.adminwavesoffood.databinding.ActivityPendingOrderBinding
import com.example.adminwavesoffood.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity(), PendingOrderAdapter.OnItemClicked {
    private lateinit var binding: ActivityPendingOrderBinding
    private var listOfName : MutableList<String> = mutableListOf()
    private var listOfTotalPrice: MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder: MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrderDetails> =arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var  databaseOrderDetails: DatabaseReference



    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = FirebaseDatabase.getInstance()
        databaseOrderDetails = database.reference.child("OrderDetails")
          getOrderDetails()


        binding.backButton.setOnClickListener{
            finish()
        }


    }

    private fun getOrderDetails() {
       databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
            for (orderSnapshot in snapshot.children){
                val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                orderDetails?.let {
                    listOfOrderItem.add(it)
                }
            }
               addDataToListForRecyclerView()
           }



           override fun onCancelled(error: DatabaseError) {

           }

       })
    }
    private fun addDataToListForRecyclerView() {
                 for (orderItem in listOfOrderItem){
                   orderItem.userName?.let{listOfName.add(it)}
                   orderItem.totalPrice?.let{listOfTotalPrice.add(it)}
                   orderItem.foodImages?.filterNot{it.isEmpty()}?.forEach {
                       listOfImageFirstFoodOrder.add(it)
                   }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = PendingOrderAdapter(this,listOfName,listOfTotalPrice,listOfImageFirstFoodOrder,this)
        binding.pendingOrderRecyclerView.adapter = adapter
    }

    override fun onItemClickListener(position: Int) {
        val intent = Intent(this, OrderdetailsActivity::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("UserOrderDetails",userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClickListener(position: Int) {
       val childItemPushKey = listOfOrderItem[position].itemPushKey
        val clickItemOrderReference = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderReference?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptStatus(position)
    }


    override fun onItemDispatchClickListener(position: Int) {
        val order = listOfOrderItem[position]
        val dispatchItemPushKey = order.itemPushKey
        val userUid = order.userUid

        // ✅ Step 1: Write to CompletedOrder
        val dispatchItemOrderReference = database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(order)
            .addOnSuccessListener {
                // ✅ Step 2: Update both paths
                val updates = HashMap<String, Any>()
                updates["CompletedOrder/$dispatchItemPushKey/orderAccepted"] = true
                updates["OrderDetails/$dispatchItemPushKey/orderAccepted"] = true
                updates["user/$userUid/BuyHistory/$dispatchItemPushKey/orderAccepted"] = true

                database.reference.updateChildren(updates)
                    .addOnSuccessListener {
                        // ✅ Step 3: Delete from OrderDetails (pending)
                        deleteThisItemFromOrderDetails(dispatchItemPushKey)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Order moved but status not updated!", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Order dispatch failed!", Toast.LENGTH_SHORT).show()
            }
    }


    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {
      val orderDetailsItemsReference = database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemsReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this,"Order is Dispatched", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
            Toast.makeText(this,"Order is Not Dispatched", Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateOrderAcceptStatus(position: Int) {
      val userIdOfClickedItem = listOfOrderItem[position].userUid
        val pushKeyOfClickedItem = listOfOrderItem[position].userUid
        val buyHistoryReference = database.reference.child("user").child(userIdOfClickedItem!!).child("BuyHistory").child(pushKeyOfClickedItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
         databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)

    }

}
