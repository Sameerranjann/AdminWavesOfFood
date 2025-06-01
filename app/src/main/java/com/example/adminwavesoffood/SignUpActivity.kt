package com.example.adminwavesoffood

import android.R.attr.tag
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminwavesoffood.databinding.ActivitySignUpBinding
import com.example.adminwavesoffood.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.core.Tag
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var userName: String
    private lateinit var nameOfResturant: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Initialize the Firebase auth and database.
        auth = Firebase.auth
        database = Firebase.database.reference




        binding.createButton.setOnClickListener {
            // get text from edit text present in activity_sign_up.xml
            userName = binding.name.text.toString().trim()
            nameOfResturant = binding.resturantName.text.toString().trim()
            email = binding.emailOrPhone.text.toString().trim()
            password = binding.password.text.toString().trim()
            if (userName.isBlank() || nameOfResturant.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Pill fill all details", Toast.LENGTH_SHORT).show()

            } else {
                createAccount(email, password)
            }


        }
        binding.alreadyHaveAnAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val locationList = arrayOf("jaipur", "Bihar", "Rajasthan", "Up")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autocompleteTextView = binding.listOfLocation
        autocompleteTextView.setAdapter(adapter)
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure", task.exception)
            }

        }
    }

    //save data into database.
    private fun saveUserData() {
        // get text from edit text present in activity_sign_up.xml
        userName = binding.name.text.toString().trim()
        nameOfResturant = binding.resturantName.text.toString().trim()
        email = binding.emailOrPhone.text.toString().trim()
        password = binding.password.text.toString().trim()
        val user = UserModel(userName, nameOfResturant, email, password)
        val userId: String = FirebaseAuth.getInstance().currentUser!!.uid

        //save user data in firebasedatabase
        database.child("user").child(userId).setValue(user)
    }

}


