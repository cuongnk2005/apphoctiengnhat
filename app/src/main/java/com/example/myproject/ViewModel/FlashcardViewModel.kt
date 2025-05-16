package com.example.myproject.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.Model.FlashcardModel
import com.example.myproject.Model.FlashcardSetInfo
import com.example.myproject.Model.OldTopic
import com.example.myproject.Repository.AuthRepository
import com.example.myproject.Repository.TopicRepository
import kotlinx.coroutines.launch


class FlashcardViewModel : ViewModel() {
    private val topicRepository = TopicRepository()
    // LiveData for current flashcard
    private var _currentFlashcard = MutableLiveData<FlashcardModel?>()
    val currentFlashcard: LiveData<FlashcardModel?> get() = _currentFlashcard
    // LiveData for progress
    private val _progressText = MutableLiveData<String>()
    val progressText: LiveData<String> get() = _progressText

    private val _progressPercentage = MutableLiveData<Int>()
    val progressPercentage: LiveData<Int> = _progressPercentage

    // List of flashcards for current set
    private var flashcards = mutableListOf<FlashcardModel>()
    private var learnFlashcards = mutableListOf<FlashcardModel>()
    // Current position in flashcard list
    private var currentPosition = 0

    // Flashcard set info
    private lateinit var flashcardSet: FlashcardSetInfo
    private val authRepository = AuthRepository()
    private val oldTopicInstance = OldTopic()
    // Load flashcards from database by set ID
    fun loadFlashcards(setId: String) {
        viewModelScope.launch {
         val topic = topicRepository.getTopicByID(setId)
            if(topic!= null){
                flashcards = topic.vocabulary_list
                learnFlashcards = flashcards.toMutableList()
                flashcardSet = FlashcardSetInfo(
                    id = 1,
                    title = "Basic Japanese vocabulary",
                    totalCards = flashcards.size,
                    learnedCards = 0,
                    level = "N5",
                    category = "Basic"
                )
                if (flashcards.isNotEmpty()) {
                    _currentFlashcard.value = flashcards[0]
                    updateProgress()
                }

            }
        }
    }


    // For testing/demo purposes
    fun loadSampleFlashcards() {
//        flashcards = getSampleFlashcards()
        flashcardSet = FlashcardSetInfo(
            id = 1,
            title = "Basic Japanese vocabulary",
            totalCards = flashcards.size,
            learnedCards = 0,
            level = "N5",
            category = "Basic"
        )

        // Set initial flashcard
        if (flashcards.isNotEmpty()) {
            _currentFlashcard.value = flashcards[0]
            updateProgress()
        }
    }

    // Move to next flashcard
    fun nextFlashcard() {
        Log.d("tessttthu", "${ _currentFlashcard.value.toString()}")
        if (learnFlashcards.size != 0){
        if (currentPosition < learnFlashcards.size - 1) {
            currentPosition++
            _currentFlashcard.value = learnFlashcards[currentPosition]
            updateProgress()
        }else{
            // End of deck, show completion or loop back
            // For now, we'll loop back to the first card
            currentPosition = 0
            _currentFlashcard.value = learnFlashcards[currentPosition]
            updateProgress()
        }
        } else {
            _currentFlashcard.value = null

        }
    }

    // Move to previous flashcard
//    fun previousFlashcard() {
//        if (currentPosition > 0) {
//            currentPosition--
//            _currentFlashcard.value = learnFlashcards[currentPosition]
//            updateProgress()
//        }
//    }

    // ham loc nhung tu da hoc
    fun markAsKnown() {
       val index = flashcards.indexOf(learnFlashcards[currentPosition])
        flashcards[index].isKnown = true
        learnFlashcards.removeAt(currentPosition)
        updateProgress()
    }

    // Reset progress for all flashcards
    fun resetAllFlashcardsProgress() {
        flashcards.forEach { it.isKnown = false }
        learnFlashcards = flashcards.toMutableList()
        updateProgress()
    }


  // lay tat ca cac flascard
    fun getAllFlashcards(): List<FlashcardModel> {
        return flashcards
    }

    // Get infor
    fun getFlashcardSetInfo(): FlashcardSetInfo {
        return flashcardSet
    }

    // update progress
    private fun updateProgress() {
        val learnedCards = flashcards.count { it.isKnown }
        val totalCards = flashcards.size
//        val currentCard = currentPosition + 1
        _progressText.value = "$learnedCards/$totalCards word"
        val progressPercentage = (learnedCards * 100) / totalCards
        _progressPercentage.value = progressPercentage

        // Update flashcard set info

        flashcardSet = flashcardSet.copy(learnedCards = learnedCards)
    }


     fun updateStatusOFLesson(id:String, status: Boolean = true){
         viewModelScope.launch {
             val listIdTopics = authRepository.getOldTopicsFromUser()
            var newListOldTopic = oldTopicInstance.changeListOldTopic(id,listIdTopics,status)
             val updatemap = mapOf<String, Any>(
                 "listTopicStuded" to newListOldTopic,
             )
             authRepository.updateUserByID(updatemap)
         }

     }
}