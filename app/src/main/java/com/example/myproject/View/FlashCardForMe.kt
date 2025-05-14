package com.example.myproject.View

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.Model.Anki
import com.example.myproject.Model.AnkiFlashCard
import com.example.myproject.Model.AnswerRatting
import com.example.myproject.R

import com.example.myproject.databinding.ActivityFlashCardForMeBinding

class FlashCardForMe : AppCompatActivity() {
    private lateinit var binding : ActivityFlashCardForMeBinding
    private var currentIndex = 0
    private var blueCount = 0
    private var redCount = 0
    private var greenCount = 0
    private var currentCard:AnkiFlashCard? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFlashCardForMeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setClickListeners()


        loadNextCard()
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
            loadNextCard()
        }
        binding.btnHard.setOnClickListener {
            recordAnswer(AnswerRatting.HARD)
            loadNextCard()
        }
        binding.btnGood.setOnClickListener {
            recordAnswer(AnswerRatting.GOOD)
            loadNextCard()
        }
        binding.btnEasy.setOnClickListener {
            recordAnswer(AnswerRatting.EASY)
            loadNextCard()
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
        binding.tvAnswer.visibility = View.INVISIBLE
        binding.tvExample.visibility = View.INVISIBLE
        binding.tvFurigana.visibility = View.INVISIBLE

        binding.btnShowAnswer.visibility = View.VISIBLE
        binding.bottomActionBar.visibility = View.GONE

        currentCard = getNextFlashCard()

        currentCard?.let { card ->
            binding.tvQuestion.text = card.tuvung
            binding.tvAnswer.text = card.meaning
            binding.tvExample.text = card.exampleJapanese
            binding.tvFurigana.text = card.romaji
        }

    }

    private fun getNextFlashCard(): AnkiFlashCard {
        val flashCard = flashCardList[currentIndex]
        currentIndex = (currentIndex + 1) % flashCardList.size // Vòng lặp lại khi hết danh sách
        return flashCard
    }

    private val flashCardList = listOf(
        AnkiFlashCard(
            id = 1,
            tuvung = "こんにちは",
            meaning = "Xin chào",
            exampleJapanese = "こんにちは、お元気ですか？",
            romaji = "Konnichiwa, o-genki desu ka?"
        ),
        AnkiFlashCard(
            id = 2,
            tuvung = "ありがとう",
            meaning = "Cảm ơn",
            exampleJapanese = "ありがとう、助かりました。",
            romaji = "Arigatou, tasukarimashita."
        ),
        AnkiFlashCard(
            id = 3,
            tuvung = "さようなら",
            meaning = "Tạm biệt",
            exampleJapanese = "さようなら、また会いましょう。",
            romaji = "Sayounara, mata aimashou."
        ),
        AnkiFlashCard(
            id = 4,
            tuvung = "おはよう",
            meaning = "Chào buổi sáng",
            exampleJapanese = "おはよう、今日はいい天気ですね。",
            romaji = "Ohayou, kyou wa ii tenki desu ne."
        )
    )

    private fun recordAnswer(rating: AnswerRatting) {
        currentCard?.let { card ->
            updateCardReviewTime(card, rating)
        }
    }

    private fun updateCardReviewTime(card: AnkiFlashCard, rating: AnswerRatting) {
        val nextReviewTime = when (rating) {
            AnswerRatting.AGAIN -> System.currentTimeMillis() + (1 * 60 * 1000) // 1 minute
            AnswerRatting.HARD -> System.currentTimeMillis() + (6 * 60 * 1000) // 6 minutes
            AnswerRatting.GOOD -> System.currentTimeMillis() + (10 * 60 * 1000) // 10 minutes
            AnswerRatting.EASY -> System.currentTimeMillis() + (4 * 24 * 60 * 60 * 1000) // 4 days
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