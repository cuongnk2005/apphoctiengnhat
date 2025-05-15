package com.example.myproject.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.Repository.AnkiRepository
import kotlinx.coroutines.launch

class AddFlashCardViewModel: ViewModel() {
    private val _bo = MutableLiveData<List<String>>()
    val bo: MutableLiveData<List<String>> get() = _bo
    private val ankiRepository = AnkiRepository()

    fun addFlashcardIntoAnki(name:String){
        viewModelScope.launch {
            ankiRepository.pushNameSetIntoAnki(name)
        }
    }
    fun getBo(){
        viewModelScope.launch {
            _bo.value = ankiRepository.getNameAnki()
        }

    }
    fun getAnkiByPosition(position:Int): String {
        var list = _bo.value
        var anki = ""
        if(list!= null && position < list.size){
            anki = list[position]
        }
        return anki
    }


    fun removeFlashcardFromAnki(name: String) {
        viewModelScope.launch {
            ankiRepository.deleteAnki(name)
        }
    }

}