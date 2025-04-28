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
<<<<<<< HEAD
=======
//    implementation("mysql:mysql-connector-java:8.0.33")
//    implementation("com.google.android.gms:play-services-auth:20.7.0")
//    implementation("com.facebook.android:facebook-login:16.0.1")
//
//
//    implementation ("de.hdodenhof:circleimageview:3.1.0")
//    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.27")
//
//    implementation ("com.google.android.material:material:1.8.0")
//
//    implementation("androidx.room:room-runtime:2.6.1")
//    implementation(libs.androidx.credentials)
//    implementation(libs.androidx.credentials.play.services.auth)
//    implementation(libs.googleid)
//    implementation(libs.firebase.storage)
//    kapt("androidx.room:room-compiler:2.6.1")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
//    implementation("androidx.activity:activity-ktx:1.8.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
//    implementation("androidx.room:room-ktx:2.6.1")
//    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
//    implementation("com.google.firebase:firebase-analytics")
//    implementation("com.google.firebase:firebase-auth")
//    implementation("com.google.firebase:firebase-messaging")
//    implementation("androidx.fragment:fragment-ktx:1.7.0")
//    implementation("com.google.firebase:firebase-firestore-ktx")
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    implementation(libs.androidx.activity)
//    implementation(libs.androidx.constraintlayout)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)


>>>>>>> ebd1ef533122542a6a98895a2d080abf4dd71a3d
    // Firebase (quản lý bằng BoM)
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-messaging")
<<<<<<< HEAD
    implementation("com.google.firebase:firebase-database")
=======
    implementation("com.google.firebase:firebase-firestore-ktx")
>>>>>>> ebd1ef533122542a6a98895a2d080abf4dd71a3d
    implementation(libs.firebase.storage)

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity)
    implementation("androidx.fragment:fragment-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // Auth
    implementation(libs.play.services.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation("com.facebook.android:facebook-login:18.0.2")

    // Other libs
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.27")
    implementation(libs.mysql.connector.java)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
