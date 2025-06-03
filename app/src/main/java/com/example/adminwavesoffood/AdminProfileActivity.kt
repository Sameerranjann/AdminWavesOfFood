package com.example.adminwavesoffood

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adminwavesoffood.databinding.ActivityAdminProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("user")

        binding.backButtonAdminProfile.setOnClickListener {
            finish()
        }
        binding.saveInfoButton.setOnClickListener {
            updateUserData()
        }
        binding.name.isEnabled = false
        binding.address.isEnabled = false
        binding.email.isEnabled = false
        binding.phoneNumber.isEnabled = false
        binding.password.isEnabled = false
        binding.saveInfoButton.isEnabled = false
        var isEnable = false

        binding.editButton.setOnClickListener {
            isEnable = !isEnable
            binding.name.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.phoneNumber.isEnabled = isEnable
            binding.password.isEnabled = isEnable
            if (isEnable) {
                binding.name.requestFocus()
            }
            binding.saveInfoButton.isEnabled = isEnable
        }

        retriveUserData()
    }

    private fun retriveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = adminReference.child(currentUserUid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val ownerName = snapshot.child("name").getValue()
                        val email = snapshot.child("email").getValue()
                        val password = snapshot.child("password").getValue()
                        val address = snapshot.child("address").getValue()
                        val phone = snapshot.child("phone").getValue()
                        setDataToTextView(ownerName, email, password, address, phone)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error here
                }
            })
        }
    }


    private fun setDataToTextView(
        ownerName: Any?,
        email: Any?,
        password: Any?,
        address: Any?,
        phone: Any?
    ) {
        binding.name.setText(ownerName.toString())
        binding.email.setText(email.toString())
        binding.password.setText(password.toString())
        binding.phoneNumber.setText(phone.toString())
        binding.address.setText(address.toString())

    }
    private fun AdminProfileActivity.updateUserData() {
         var updateName =   binding.name.text.toString()
        var updateEmail = binding.email.text.toString()
        var updatePassword =  binding.password.text.toString()
        var updatePhone = binding.phoneNumber.text.toString()
         var updateAddress = binding.address.text.toString()
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid !=null){

            val userReference = adminReference.child(currentUserUid)
            userReference.child("name").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("password").setValue(updatePassword)
            userReference.child("phone").setValue(updatePhone)
            userReference.child("address").setValue(updateAddress)


            Toast.makeText(this,"Profiled updated successfully😊", Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)
        }

        else {
            Toast.makeText(this,"Profiled updated fail😒", Toast.LENGTH_SHORT).show()
        }
    }

}


