package com.example.adminwavesoffood

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwavesoffood.adapter.MenuItemAdapter
import com.example.adminwavesoffood.databinding.ActivityAllItemBinding
import com.example.adminwavesoffood.model.AllMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class AllItemActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database : FirebaseDatabase
    private  var menuItems : kotlin.collections.ArrayList<AllMenu> =ArrayList()
    private val binding : ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        databaseReference=FirebaseDatabase.getInstance().reference
        retriveMenuItem()
    }

    private fun retriveMenuItem() {
      database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")

        //fetch data from databases.
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{


            override fun onDataChange(snapshot: DataSnapshot){
                menuItems.clear()


                for (foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let{
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError","Error: ${error.message}")
            }
        })

    }
    private fun setAdapter() {
        val adapter = MenuItemAdapter(this@AllItemActivity,menuItems,databaseReference)
        binding.MenuRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.MenuRecyclerview.adapter = adapter
    }
}