package com.example.myproject.Model

import com.cloudinary.android.MediaManager
import android.content.Context

object  CloudinaryHelper {
    private var initalized = false
    fun init(context: Context) {
        if (!initalized) {
            // Lấy thông tin config từ Cloudinary Dashboard
            val config = mapOf(
                "cloud_name" to "dwcwgm88h",
                "api_key" to "153933832421589",
//            "api_secret" to "your_api_secret"  Không dùng cho upload unsigned
            )
            MediaManager.init(context, config)
            initalized = true
        }
    }
}