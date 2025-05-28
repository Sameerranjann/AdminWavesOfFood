package com.example.adminwavesoffood

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwavesoffood.adapter.AddItemAdapter
import com.example.adminwavesoffood.databinding.ActivityAllItemBinding
import java.util.ArrayList

class AllItemActivity : AppCompatActivity() {
    private val binding : ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val menuFoodName = listOf("Burger","momo","Drink","Fried")
        val menuItemPrice = listOf("13","10","22","18")
        val menuImage = listOf(R.drawable.foodmenu,R.drawable.foodmenu,R.drawable.foodmenu,R.drawable.foodmenu,R.drawable.foodmenu)

        val adapter = AddItemAdapter(ArrayList(menuFoodName),ArrayList(menuItemPrice),ArrayList(menuImage))
        binding.MenuRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.MenuRecyclerview.adapter = adapter


    }
}