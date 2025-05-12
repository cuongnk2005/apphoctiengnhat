package com.example.myproject.View

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.databinding.ActivityFormAddFlashCardBinding

class FormAddFlashCard : AppCompatActivity() {
    private lateinit var binding: ActivityFormAddFlashCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFormAddFlashCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}