package com.example.myproject.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.Repository.ankiRepository
import kotlinx.coroutines.launch

class AddFlashCardViewModel: ViewModel() {
//    private val _flashCards = MutableLiveData<List<FlashCard>>()
//    val flashCards: LiveData<List<FlashCard>> = _flashCards
private val ankiRepository = ankiRepository()
    fun addFlashcardIntoAnki(name:String){
        viewModelScope.launch {
            ankiRepository.pushNameSetIntoAnki(name)
        }

    }
}