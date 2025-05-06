package com.example.myproject.View

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myproject.R
import com.example.myproject.databinding.ActivityLearnByFlashcardBinding

class LearnByFlashcard : AppCompatActivity() {
    private lateinit var binding: ActivityLearnByFlashcardBinding

    // Biến lưu trạng thái thẻ (lật hay không)
    private var isFlipped = false

    // Hiệu ứng lật
    private lateinit var frontToBackAnimation: AnimatorSet
    private lateinit var backToFrontAnimation: AnimatorSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLearnByFlashcardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lấy ID của bộ thẻ từ intent
        val deckId = intent.getLongExtra("DECK_ID", -1L)
        if (deckId == -1L) {
            finish()
            return
        }

        // Khởi tạo hiệu ứng lật
        setupCardFlipAnimations()

    }

    private fun setupCardFlipAnimations() {
        // Thiết lập camera distance cho hiệu ứng 3D
        val scale = resources.displayMetrics.density
        binding.frontCard.cameraDistance = 8000 * scale
        binding.backCard.cameraDistance = 8000 * scale

        // Load hiệu ứng lật từ tệp XML
        frontToBackAnimation = AnimatorInflater.loadAnimator(
            this,
            R.animator.front_to_back
        ) as AnimatorSet

        backToFrontAnimation = AnimatorInflater.loadAnimator(
            this,
            R.animator.back_to_front
        ) as AnimatorSet
    }
}