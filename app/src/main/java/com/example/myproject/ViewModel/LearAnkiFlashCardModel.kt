package com.example.myproject.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.Model.AnkiFlashCard
import com.example.myproject.Model.AnkiScheduler
import com.example.myproject.Repository.AnkiRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class LearAnkiFlashCardModel: ViewModel() {
    private val ankiRepository = AnkiRepository()
    private val _ankiflashcard = MutableLiveData<List<AnkiFlashCard>>()
    val ankiFlashCard : LiveData<List<AnkiFlashCard>> get() = _ankiflashcard
    private var _currentCard = MutableLiveData<AnkiFlashCard?>()
    val currentCard : LiveData<AnkiFlashCard?> get() = _currentCard
    private var currentpoint = 0
    private var listFlashCard = mutableListOf<AnkiFlashCard>()
    private var listPrioritize = mutableListOf<AnkiFlashCard>()
    private var ankiScheduler = AnkiScheduler()
    private var nameAnki = ""
    fun getAnkiFlashCardByName(name: String){
        this.nameAnki = name
        viewModelScope.launch {
            _ankiflashcard.value = ankiRepository.getAnkiFlasCardByNameForLear(name)
            val now = LocalDateTime.now()
            listFlashCard = _ankiflashcard.value?.sortedWith(compareBy(
                { LocalDateTime.parse(it.nextReviewDate).isAfter(now.plusMinutes(1)) },
                { LocalDateTime.parse(it.nextReviewDate).isAfter(now.plusMinutes(10)) },
                { LocalDateTime.parse(it.nextReviewDate) }
            ))?.toMutableList() ?: mutableListOf()
            Log.d("jkfsf", "${listFlashCard.size}")
            if (listFlashCard.isNotEmpty()) {
                _currentCard.value = listFlashCard[currentpoint]
            } else {
                _currentCard.value = null
            }
        }
    }
    fun getNextCurrentCard() {
        if (listFlashCard.isNotEmpty()) {
            currentpoint++
            if (currentpoint >= listFlashCard.size) {
                currentpoint = 0
            }
            _currentCard.value = listFlashCard[currentpoint]
        }
    }
    fun updateCurrentFlashCard(card:AnkiFlashCard, rating: Int){
        Log.d("sjdkhvjk", "co chay ham nay")
        val newCard = ankiScheduler.reviewCard(card,rating)
        Log.d("sjdkhvjsdfsk", "co chay ham nay")
        listFlashCard.removeAt(currentpoint)
        listFlashCard.add(currentpoint,newCard)
        Log.d("ssdfsk", "co chay ham nay")

        Log.d("srgsfasdfsk", "co chay ham nay")
        ankiRepository.UpdateAnkiFlasCardByName(nameAnki,newCard)
        getAnkiFlashCardByName(nameAnki)


    }
}