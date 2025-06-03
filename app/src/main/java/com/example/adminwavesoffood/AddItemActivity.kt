package com.example.adminwavesoffood

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.adminwavesoffood.databinding.ActivityAddItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddItemActivity : AppCompatActivity() {

    // Food item details
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String
    private var foodImageUri: Uri? = null

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButtonAddItem.setOnClickListener {
            finish()
        }

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        binding.addItemButton.setOnClickListener {
            foodName = binding.enterFoodName.text.toString().trim()
            foodPrice = binding.enterFoodPrice.text.toString().trim()
            foodDescription = binding.description.text.toString().trim()
            foodIngredient = binding.ingredients.text.toString().trim()

            if (foodName.isNotBlank() && foodPrice.isNotBlank() && foodDescription.isNotBlank() && foodIngredient.isNotBlank() && foodImageUri != null) {
                uploadImageToCloudinary()
            } else {
                Toast.makeText(this, "Please fill all details and select an image", Toast.LENGTH_SHORT).show()
            }
        }

        binding.selectimage.setOnClickListener {
            pickImage.launch("image/*")
        }


    }



    // Upload image to Cloudinary
    private fun uploadImageToCloudinary() {
        val requestId = MediaManager.get().upload(foodImageUri)
            .unsigned("android_unsigned")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    Toast.makeText(this@AddItemActivity, "Uploading image...", Toast.LENGTH_SHORT).show()
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    val imageUrl = resultData?.get("secure_url").toString()
                    uploadItemToFirebase(imageUrl)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Toast.makeText(this@AddItemActivity, "Upload failed: ${error?.description}", Toast.LENGTH_SHORT).show()
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            }).dispatch()
    }

    // Upload item data to Firebase
    private fun uploadItemToFirebase(imageUrl: String) {
        val menuRef = database.getReference("menu")
        val newItemKey = menuRef.push().key

        if (newItemKey != null) {
            val itemData = mapOf(
                "key" to newItemKey,  // <-- yaha key ko add karo
                "name" to foodName,
                "price" to foodPrice,
                "description" to foodDescription,
                "ingredients" to foodIngredient,
                "imageUrl" to imageUrl
            )
            menuRef.child(newItemKey).setValue(itemData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save item", Toast.LENGTH_SHORT).show()
                }
        }
    }


    // Image picker
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            foodImageUri = uri
            binding.selectedimage.setImageURI(uri)
        }
    }
}
