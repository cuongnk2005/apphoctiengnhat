package com.example.myproject.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val received = intent.getStringExtra("sendInfo")

        events()

    }

    private fun events() {
        binding.btnBack.setOnClickListener {
            val intent = Intent()
            intent.putExtra("reply", intent.getStringExtra("senInfo"))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}