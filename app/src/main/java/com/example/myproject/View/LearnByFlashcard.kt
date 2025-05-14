package com.example.myproject.View

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myproject.R
import com.example.myproject.databinding.ActivityLearnByFlashcardBinding

import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.myproject.Model.FlashcardModel
import com.example.myproject.Model.ToastType
import com.example.myproject.ViewModel.FlashcardViewModel
import kotlinx.coroutines.delay
import java.util.Locale

class LearnByFlashcard : AppCompatActivity() {
    private lateinit var binding: ActivityLearnByFlashcardBinding
    private val viewModel: FlashcardViewModel by viewModels()
    // Biến lưu trạng thái thẻ (lật hay không)
    private var isFlipped = false

    // Hiệu ứng lật
    private lateinit var frontToBackAnimation: AnimatorSet
    private lateinit var backToFrontAnimation: AnimatorSet
    private var isFrontVisible = true
    private lateinit var tts: TextToSpeech
    private var mediaPlayer: MediaPlayer? = null
    private var flashcardSetId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLearnByFlashcardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup animations
        setupCardAnimations()

        // Get flashcard set ID from intent if available
         flashcardSetId = intent.getStringExtra("FLASHCARD_SET_ID").toString()
        Log.d("eeeeeee", "$flashcardSetId")
        if (flashcardSetId != null) {
            viewModel.loadFlashcards(flashcardSetId)

        } else {
            // For testing purposes, load sample data if no ID provided
            viewModel.loadSampleFlashcards()
        }

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        // Quan sát thẻ flash card hiện tại
        viewModel.currentFlashcard.observe(this) { flashcard ->
            if(flashcard!= null){
                updateFlashcardDisplay(flashcard)
            } else {
            viewModel.updateStatusOFLesson(flashcardSetId)
                onBackPressed()
                showCustomToast(
                    context = this,
                    title = "Thông báo",
                    message = "Hoàn thành bài học!",
                    type = ToastType.SUCCESS
                )
            }
        }

        // Observe progress
        viewModel.progressText.observe(this) { progressText ->
            binding.progressText.text = progressText
        }

        // Observe progress percentage
        viewModel.progressPercentage.observe(this) { percentage ->
            binding.lessonProgress.progress = percentage
        }
    }

    private fun updateFlashcardDisplay(flashcard: FlashcardModel) {
        // Reset card to front side when changing cards
        if (!isFrontVisible) {
            flipCard()
        }

        // Cập nhật thông tin mặt trước của thẻ khi thay đổi
        binding.kanjiDisplay.text = flashcard.kanji
        binding.japaneseWord.text = flashcard.kanji
        binding.hiraganaText.text = flashcard.hiragana
        binding.romajiText.text = flashcard.romaji

        // Cập nhật thông tin mặt sau của thẻ khi thay đổi
        binding.meaningText.text = flashcard.meaning
        binding.definitionText.text = flashcard.definition
        binding.exampleJapanese.text = flashcard.exampleJapanese
        binding.exampleReading.text = flashcard.exampleReading
        binding.exampleMeaning.text = flashcard.exampleMeaning
    }

    private fun setupClickListeners() {

        binding.btnAudio.setOnClickListener{
            tts = TextToSpeech(this) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts.language = Locale.JAPAN
                    var nihongo = viewModel.currentFlashcard?.value?.hiragana
                    tts.speak(nihongo, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }

        }

        // Back button
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // More options button
        binding.btnMore.setOnClickListener {
            showOptionsMenu()
        }

        // Flip card (front to back)
        binding.frontCard.setOnClickListener {
            if (isFrontVisible) {
                flipCard()
            }
        }

        // Flip card (back to front)
        binding.backCard.setOnClickListener {
            if (!isFrontVisible) {
                flipCard()
            }
        }



        // Navigation buttons
//        binding.btnPrevious.setOnClickListener {
//            viewModel.previousFlashcard()
//        }

        binding.btnNext.setOnClickListener {
            viewModel.nextFlashcard()
        }

        // Knowledge tracking buttons
        binding.btnKnow.setOnClickListener {
            viewModel.markAsKnown()
            moveToNextCard()
        }

        binding.btnDontKnow.setOnClickListener {
//            viewModel.markAsUnknown()
            moveToNextCard()
        }
    }

    private fun setupCardAnimations() {
        // Load animations
        val scale = resources.displayMetrics.density
        binding.frontCard.cameraDistance = 8000 * scale
        binding.backCard.cameraDistance = 8000 * scale

        frontToBackAnimation = AnimatorInflater.loadAnimator(
            this,
            R.animator.front_to_back
        ) as AnimatorSet

        backToFrontAnimation = AnimatorInflater.loadAnimator(
            this,
            R.animator.back_to_front
        ) as AnimatorSet
    }

    private fun flipCard() {
        isFrontVisible = if (isFrontVisible) {
            // Flip to back
            frontToBackAnimation.setTarget(binding.frontCard)
            backToFrontAnimation.setTarget(binding.backCard)
            frontToBackAnimation.start()
            backToFrontAnimation.start()
            binding.frontCard.visibility = View.GONE
            binding.backCard.visibility = View.VISIBLE
            false
        } else {
            // Flip to front
            frontToBackAnimation.setTarget(binding.backCard)
            backToFrontAnimation.setTarget(binding.frontCard)
            backToFrontAnimation.start()
            frontToBackAnimation.start()
            binding.backCard.visibility = View.GONE
            binding.frontCard.visibility = View.VISIBLE
            true
        }
    }

//    private fun playPronunciation() {
//        try {
//            // Get current flashcard audio resource
////            val audioResId = viewModel.getCurrentFlashcardAudioResourceId()
//
//            // Release previous media player if exists
//            mediaPlayer?.release()
//
//            // Create and start new media player
////            mediaPlayer = MediaPlayer.create(this, audioResId)
////            mediaPlayer?.start()
//
//            // Set completion listener to release resources
//            mediaPlayer?.setOnCompletionListener {
//                it.release()
//            }
//        } catch (e: Exception) {
////            Toast.makeText(this, "Không thể phát âm thanh", Toast.LENGTH_SHORT).show()
//            showCustomToast(
//                context = this,
//                title = "Thông báo",
//                message = "Chức năng phát âm thanh đang phát triển",
//                type = ToastType.INFO
//            )
//        }
//    }

    private fun moveToNextCard() {
        // Thêm độ trễ nhỏ để người dùng kịp nhận phản hồi trước khi đổi thẻ.
        binding.root.postDelayed({
            viewModel.nextFlashcard()
        }, 300)
    }

    private fun showOptionsMenu() {
        val options = arrayOf("Đánh dấu tất cả là chưa học", "Phát tất cả âm thanh", "Xem tất cả từ", "Thông tin bộ thẻ")

        AlertDialog.Builder(this)
            .setTitle("Tùy chọn")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        viewModel.updateStatusOFLesson(flashcardSetId,false)
                        viewModel.resetAllFlashcardsProgress()}
                    1 -> {
                        showCustomToast(
                            context = this,
                            title = "Thông báo",
                            message = "Chức năng đang phát triển",
                            type = ToastType.INFO
                        )
                    }
                    2 -> showAllFlashcardsDialog()
                    3 -> showFlashcardSetInfo()
                }
            }
            .show()
    }

    private fun showAllFlashcardsDialog() {
        val flashcards = viewModel.getAllFlashcards()
        val flashcardTexts = flashcards.map { "${it.kanji} - ${it.hiragana} - ${it.meaning}" }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Tất cả từ vựng")
            .setItems(flashcardTexts, null)
            .setPositiveButton("Đóng", null)
            .show()
    }

    private fun showFlashcardSetInfo() {
        val setInfo = viewModel.getFlashcardSetInfo()
        AlertDialog.Builder(this)
            .setTitle(setInfo.title)
            .setMessage("Tổng số từ: ${setInfo.totalCards}\n" +
                    "Đã học: ${setInfo.learnedCards}\n" +
                    "Cấp độ: ${setInfo.level}\n" +
                    "Chủ đề: ${setInfo.category}")
            .setPositiveButton("Đóng", null)
            .show()
    }

    private fun showCustomToast(context: Context, title: String, message: String, type: ToastType) {
        val layout = LayoutInflater.from(context).inflate(R.layout.custom_toast, null)

        // Thiết lập nội dung
        layout.findViewById<TextView>(R.id.toast_title).text = title
        layout.findViewById<TextView>(R.id.toast_message).text = message

        // Thiết lập icon và màu sắc dựa trên loại thông báo
        val iconView = layout.findViewById<ImageView>(R.id.toast_icon)
        val container = layout.findViewById<LinearLayout>(R.id.custom_toast_container)

        when (type) {
            ToastType.SUCCESS -> {
                iconView.setImageResource(R.drawable.ic_success)
                container.background = ContextCompat.getDrawable(context, R.drawable.toast_success_bg)
            }
            ToastType.ERROR -> {
                iconView.setImageResource(R.drawable.ic_error)
                container.background = ContextCompat.getDrawable(context, R.drawable.toast_error_bg)
            }
            ToastType.WARNING -> {
                iconView.setImageResource(R.drawable.ic_warning)
                container.background = ContextCompat.getDrawable(context, R.drawable.toast_warning_bg)
            }
            ToastType.INFO -> {
                iconView.setImageResource(R.drawable.ic_info)
                container.background = ContextCompat.getDrawable(context, R.drawable.toast_info_bg)
            }
        }
        // Tạo và hiển thị toast
        val toast = Toast(context)
        toast.setGravity(Gravity.TOP, 0, 100)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()

        // Xử lý nút đóng
        layout.findViewById<ImageView>(R.id.close_button).setOnClickListener {
            toast.cancel()
        }
    }

    override fun onDestroy() {
        // Release media player resources
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
}