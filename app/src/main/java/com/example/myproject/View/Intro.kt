package com.example.myproject.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myproject.R

class Intro : AppCompatActivity() {
    private val intro: Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro)

        // Hide action bar if present
        supportActionBar?.hide()

        // Set full screen (optional)
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        */

        // Navigate to main activity after delay
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, UI_Login::class.java)
            startActivity(intent)
            finish()  // Close splash activity so it's not in back stack
        }, intro)
    }
}