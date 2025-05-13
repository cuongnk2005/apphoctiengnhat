package com.example.myproject.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.Model.AnkiFlashCard
import com.example.myproject.Model.FlashcardModel
import com.example.myproject.Repository.ankiRepository
import kotlinx.coroutines.launch

class FormAddFlashCardViewModel: ViewModel() {
    private val ankiRepository = ankiRepository()
    private val _bo = MutableLiveData<List<String>>()
     val bo: MutableLiveData<List<String>> get() = _bo

    fun pushFlashCardIntoAnki(name:String, flasCard: AnkiFlashCard){
  viewModelScope.launch {
      ankiRepository.pushflashcardIntoAnki(name, flasCard)
  }
    }
    fun getBo(){
        viewModelScope.launch {
            _bo.value = ankiRepository.getNameAnki()
        }

    }

}