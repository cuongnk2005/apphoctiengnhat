package com.example.myproject.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.Repository.AnkiRepository
import kotlinx.coroutines.launch
import com.example.myproject.Model.CardState
class AddFlashCardViewModel: ViewModel() {
    private val _bo = MutableLiveData<List<String>>()
    val bo: MutableLiveData<List<String>> get() = _bo
    private val _countState = MutableLiveData<List<List<Int>>>()
    val countState: MutableLiveData<List<List<Int>>> get() = _countState
    private val ankiRepository = AnkiRepository()
    fun addFlashcardIntoAnki(name: String) {
            ankiRepository.pushNameSetIntoAnki(name)
    }

    fun getBo() {
        viewModelScope.launch {
            _bo.value = ankiRepository.getNameAnki()
        }
    }

    fun getAnkiByPosition(position: Int): String {
        var list = _bo.value
        var anki = ""
        if (list != null && position < list.size) {
            anki = list[position]
        }
        return anki
    }

    fun getListValue() {
      viewModelScope.launch {
          var listvalue =  mutableListOf<MutableList<Int>>()
         _bo.value?.forEach { item ->
             var ListsubValue =  mutableListOf(0,0,0)
            var listankiFlashcard = ankiRepository.getAnkiFlasCardByNameForLear(item)
             val validCards = listankiFlashcard.filter { it.id.isNotBlank() }
             if (validCards.size > 0) {
                 if(listankiFlashcard[0].id != ""){
                     ListsubValue[0]= listankiFlashcard.count { it.state ==CardState.NEW  }
                 var coutred = listankiFlashcard.count { it.state ==CardState.RELEARNING }
                 coutred += listankiFlashcard.count { it.state ==CardState.LEARNING }
                 ListsubValue[1] =coutred
                 ListsubValue[2] = listankiFlashcard.count { it.state ==CardState.REVIEW }
                 }
             }
             listvalue.add(ListsubValue)
         }
          _countState.value = listvalue
      }
    }
        fun removeFlashcardFromAnki(name: String) {
            viewModelScope.launch {
                ankiRepository.deleteAnki(name)
            }
        }


}