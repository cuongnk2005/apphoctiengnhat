package com.example.myproject.View

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.Model.Anki
import com.example.myproject.Model.AnkiFlashCard
import com.example.myproject.Model.AnswerRatting
import com.example.myproject.R
import com.example.myproject.ViewModel.LearAnkiFlashCardModel

import com.example.myproject.databinding.ActivityFlashCardForMeBinding
import java.util.Locale

class FlashCardForMe : AppCompatActivity() {
    private lateinit var binding : ActivityFlashCardForMeBinding
    private var currentIndex = 0
    private var blueCount = 0
    private var redCount = 0
    private var greenCount = 0
    private lateinit var tts: TextToSpeech
    private var currentCard:AnkiFlashCard? = null
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
                tts = TextToSpeech(this) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        tts.language = Locale.JAPAN
                        var nihongo = currentCard.tuvung
                        tts.speak(nihongo, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                }
            } else {
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

//    private fun getNextFlashCard(): AnkiFlashCard {
//        val flashCard = flashCardList[currentIndex]
//        currentIndex = (currentIndex + 1) % flashCardList.size // Vòng lặp lại khi hết danh sách
//        return flashCard
//    }

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

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}