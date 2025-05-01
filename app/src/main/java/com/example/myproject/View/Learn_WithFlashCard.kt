package com.example.myproject.View

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myproject.R
import com.example.myproject.databinding.ActivityLearnWithFlashCardBinding

class Learn_WithFlashCard : AppCompatActivity() {
    private lateinit var binding: ActivityLearnWithFlashCardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
   binding = ActivityLearnWithFlashCardBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}