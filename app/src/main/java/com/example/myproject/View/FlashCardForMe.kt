package com.example.myproject.View

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myproject.Model.CardState
import com.example.myproject.Model.AnkiFlashCard
import com.example.myproject.Model.AnkiScheduler
import com.example.myproject.Model.AnswerRatting
import com.example.myproject.Model.ToastType

import com.example.myproject.R
import com.example.myproject.ViewModel.LearAnkiFlashCardModel

import com.example.myproject.databinding.ActivityFlashCardForMeBinding
import java.util.Locale

class FlashCardForMe : AppCompatActivity() {
    private lateinit var binding : ActivityFlashCardForMeBinding

    private lateinit var tts: TextToSpeech
    private var currentCard:AnkiFlashCard? = null
    private val ankiScheduler = AnkiScheduler()
    private val learFlashCardViewModel : LearAnkiFlashCardModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFlashCardForMeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setClickListeners()
        loadNextCard()
        var name = intent.getStringExtra("FLASHCARD_SET_Name").toString()

        if (name != "") {
            learFlashCardViewModel.getAnkiFlashCardByName(name)
        }
        observeViewModel()
    }
    private fun observeViewModel(){
        learFlashCardViewModel.currentCard.observe(this){currentCard ->
            Log.d("eeeeeee", "co chay lan dau")
            if(currentCard != null){
                this.currentCard = currentCard
                Log.d("tthjkf", "co chay trong ham nay luon")
                binding.tvAnswer.visibility = View.INVISIBLE
                binding.tvExample.visibility = View.INVISIBLE
                binding.tvFurigana.visibility = View.INVISIBLE
                binding.btnShowAnswer.visibility = View.VISIBLE
                binding.bottomActionBar.visibility = View.GONE
                binding.tvQuestion.text = currentCard.tuvung
                binding.tvAnswer.text = currentCard.meaning
                binding.tvExample.text = currentCard.exampleJapanese
                binding.tvFurigana.text = currentCard.romaji
                binding.progressBar.visibility = View.GONE
                binding.framelayout.visibility = View.VISIBLE
                if(currentCard.state == CardState.REVIEW){
                    val list = ankiScheduler.handleReviewVersion2(currentCard)
                    binding.btnAgain.text = list[0]
                    binding.btnHard.text = list[1]
                    binding.btnGood.text = list[2]
                    binding.btnEasy.text = list[3]
                } else {
                    binding.btnAgain.text = ">1ph\nHọc lại"
                    binding.btnHard.text = ">10ph \n Khó"
                    binding.btnGood.text = ">1ng \n Tốt"
                    binding.btnEasy.text = ">2ng \n Dễ"
                }
                tts = TextToSpeech(this) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        tts.language = Locale.JAPAN
                        var nihongo = currentCard.tuvung
                        tts.speak(nihongo, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                }
            } else {
                showCustomToast(
                    context = this,
                    title = "Thông báo",
                    message = "Hoàn thành bài học",
                    type = ToastType.SUCCESS
                )
                onBackPressed()
            }
        }
    }

    private fun setClickListeners() {
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.btnShowAnswer.setOnClickListener {
            showAnswer()
        }

        binding.btnAgain.setOnClickListener {

            recordAnswer(AnswerRatting.AGAIN)

        }
        binding.btnHard.setOnClickListener {
            recordAnswer(AnswerRatting.HARD)
        }
        binding.btnGood.setOnClickListener {

            recordAnswer(AnswerRatting.GOOD)

        }
        binding.btnEasy.setOnClickListener {

            recordAnswer(AnswerRatting.EASY)

        }
        binding.undoButton.setOnClickListener {
            resetCurrentCard()
        }
    }

    private fun resetCurrentCard() {
        binding.tvAnswer.visibility = View.INVISIBLE
        binding.tvExample.visibility = View.INVISIBLE
        binding.tvFurigana.visibility = View.INVISIBLE

        binding.btnShowAnswer.visibility = View.VISIBLE
        binding.bottomActionBar.visibility = View.GONE
    }
    private fun showAnswer() {
        binding.tvAnswer.visibility = View.VISIBLE
        binding.tvExample.visibility = View.VISIBLE
        binding.tvFurigana.visibility = View.VISIBLE

        binding.btnShowAnswer.visibility = View.GONE
        binding.bottomActionBar.visibility = View.VISIBLE
    }

    private fun loadNextCard() {
        learFlashCardViewModel.getNextCurrentCard()
    }



    private fun recordAnswer(rating: AnswerRatting) {
        currentCard?.let { card ->
            updateCardReviewTime(card, rating)
        }
    }

    private fun updateCardReviewTime(card: AnkiFlashCard, rating: AnswerRatting) {
        Log.d("jkhx", "cos chay ham nayf")
        when (rating) {
            AnswerRatting.AGAIN -> learFlashCardViewModel.updateCurrentFlashCard(card, 0)
            AnswerRatting.HARD -> learFlashCardViewModel.updateCurrentFlashCard(card, 1)
            AnswerRatting.GOOD -> learFlashCardViewModel.updateCurrentFlashCard(card, 2)
            AnswerRatting.EASY -> learFlashCardViewModel.updateCurrentFlashCard(card, 3)
        }

    }


    // khoi tao menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.learn_flashcard_for_me, menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_info -> {

                true
            }
            R.id.action_reset -> {

                true
            }
            R.id.action_delete -> {
                learFlashCardViewModel.deleteFlashCard()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

}