package com.example.myproject.Model

import android.app.Application
import android.graphics.Point
import com.imagekit.android.ImageKit
import com.imagekit.android.entity.TransformationPosition
import com.imagekit.android.entity.UploadPolicy
import com.imagekit.android.preprocess.ImageUploadPreprocessor

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Khởi tạo ImageKit

        ImageKit.init(
            context = applicationContext,
            publicKey = "your_public_api_key",
            urlEndpoint = "https://ik.imagekit.io/your_imagekit_id",
            transformationPosition = TransformationPosition.PATH,

        )

        // Bạn có thể khởi tạo thêm các thư viện hoặc đối tượng toàn cục ở đây
    }
}