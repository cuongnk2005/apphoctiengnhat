plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.myproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myproject"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/**",
                "google/protobuf/any.proto",
                "google/protobuf/descriptor.proto",
                "google/protobuf/duration.proto",
                "google/protobuf/empty.proto",
                "google/protobuf/field_mask.proto",
                "google/protobuf/struct.proto",
                "google/protobuf/timestamp.proto",
                "google/protobuf/wrappers.proto"
            )
        }
    }
}

dependencies {

    // Retrofit cho gọi API
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp để quản lý các yêu cầu HTTP


    // Coroutines cho xử lý bất đồng bộ


    // Firebase (quản lý bằng BoM)
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-database")
    implementation(libs.firebase.storage)

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation(libs.androidx.recyclerview)
    implementation(libs.play.services.cast.tv)
    implementation(libs.androidx.espresso.core)
    implementation(libs.play.services.fido)
    implementation(libs.firebase.firestore.ktx)
    kapt("androidx.room:room-compiler:2.6.1")

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity)
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.fragment:fragment-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // ViewModel và LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")

    // Auth
    implementation(libs.play.services.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation("com.facebook.android:facebook-login:18.0.2")

    // Other libs
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.27")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    // OkHttp để quản lý các yêu cầu HTTP
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // ImageKit
    implementation("com.github.imagekit-developer.imagekit-android:imagekit-android:3.0.1")
    implementation("com.github.imagekit-developer.imagekit-android:imagekit-glide-extension:3.0.1")
    implementation("com.github.imagekit-developer.imagekit-android:imagekit-picasso-extension:3.0.1")
    implementation("com.github.imagekit-developer.imagekit-android:imagekit-coil-extension:3.0.1")
    implementation("com.github.imagekit-developer.imagekit-android:imagekit-fresco-extension:3.0.1")

    // thu vien cloudinary
    implementation("com.cloudinary:cloudinary-android:2.3.1")
    // Glide (optional - để hiển thị ảnh)
    implementation("com.github.bumptech.glide:glide:4.12.0")

    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.8.21")


}
