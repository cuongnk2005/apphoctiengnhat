package com.example.myproject.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.Model.DictionaryEntry
import com.example.myproject.Repository.DictionaryRepository
import kotlinx.coroutines.launch

class DictionaryViewModel(private val repository: DictionaryRepository) : ViewModel() {

    private val _wordResult = MutableLiveData<DictionaryEntry?>()
    val wordResult: LiveData<DictionaryEntry?> = _wordResult


//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isOnline = MutableLiveData<Boolean>()
    val isOnline: LiveData<Boolean> = _isOnline

    // Tìm kiếm
    fun searchWord(query: String) {
        viewModelScope.launch {
            try {
//                _isLoading.value = true
                _errorMessage.value = null

                val result = repository.searchWord(query)
                _wordResult.value = result

                if (result == null) {
                    _errorMessage.value = "Không tìm thấy từ \"$query\""
                }
            } catch (e: Exception) {
                _errorMessage.value = "Có lỗi xảy ra: ${e.message}"
                _wordResult.value = null
            } finally {
//                _isLoading.value = false
            }
        }
    }


    fun clearError() {
        _errorMessage.value = null
    }


    fun setOnlineStatus(isOnline: Boolean) {
        _isOnline.value = isOnline
    }
}