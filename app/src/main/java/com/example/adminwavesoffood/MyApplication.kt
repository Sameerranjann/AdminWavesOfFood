package com.example.adminwavesoffood

import android.app.Application
import com.cloudinary.android.MediaManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = hashMapOf(
            "cloud_name" to "dva1xkzye",
            "api_key" to "284631695619469",
            "upload_preset" to "android_unsigned"
        )

        try {
            MediaManager.init(this, config)
        } catch (e: IllegalStateException) {
            // Already initialized, no action needed
        }
    }
}
