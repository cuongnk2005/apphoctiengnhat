package com.example.myproject.Model
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

class ImageUploader {
    companion object {
        private const val SERVER_URL = "https://192.168.1.5/php/Server/auth.php" // Thay bằng URL server PHP của bạn
        private const val API_KEY = "cuongdepzai123123" // Phải trùng với key trên server

        fun uploadToServer(
            imageFile: File,
            onSuccess: (String) -> Unit,
            onError: (String) -> Unit
        ) {
            val client = OkHttpClient()

            // Tạo request body multipart
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file",
                    imageFile.name,
                    imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                )
                .build()

            // Tạo request
            val request = Request.Builder()
                .url(SERVER_URL)
                .addHeader("X-API-KEY", API_KEY) // Header xác thực
                .post(requestBody)
                .build()

            // Thực hiện request bất đồng bộ
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    onError("Upload failed: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) {
                            val error = response.body?.string() ?: "Unknown error"
                            onError("Upload error: ${response.code} - $error")
                            return
                        }

                        try {
                            val json = response.body?.string()?.let { JSONObject(it) }
                            json?.let {
                                if (it.getBoolean("success")) {
                                    onSuccess(it.getString("url"))
                                } else {
                                    onError(it.getString("error"))
                                }
                            } ?: onError("Empty response")
                        } catch (e: Exception) {
                            onError("Parse error: ${e.message}")
                        }
                    }
                }
            })
        }
    }
}