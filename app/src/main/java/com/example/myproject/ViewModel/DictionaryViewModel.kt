package com.example.myproject.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.Model.DictionaryEntry
import com.example.myproject.Repository.DictionaryRepository
import kotlinx.coroutines.launch

class DictionaryViewModel(private val repository: DictionaryRepository) : ViewModel() {

    // LiveData cho một từ
    private val _wordResult = MutableLiveData<DictionaryEntry?>()
    val wordResult: LiveData<DictionaryEntry?> = _wordResult

    // LiveData cho danh sách từ
    private val _wordsList = MutableLiveData<List<DictionaryEntry>>()
    val wordsList: LiveData<List<DictionaryEntry>> = _wordsList

    // LiveData cho trạng thái loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData cho thông báo lỗi
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // LiveData cho trạng thái mạng
    private val _isOnline = MutableLiveData<Boolean>()
    val isOnline: LiveData<Boolean> = _isOnline

    // Tìm kiếm một từ
    fun searchWord(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
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
                _isLoading.value = false
            }
        }
    }

    // Tìm kiếm nhiều từ
    fun searchWords(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                val results = repository.searchWords(query)
                _wordsList.value = results

                if (results.isEmpty()) {
                    _errorMessage.value = "Không tìm thấy kết quả nào cho \"$query\""
                }
            } catch (e: Exception) {
                _errorMessage.value = "Có lỗi xảy ra: ${e.message}"
                _wordsList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Xóa thông báo lỗi
    fun clearError() {
        _errorMessage.value = null
    }

    // Kiểm tra lần cuối cùng tìm kiếm là online hay offline
    fun setOnlineStatus(isOnline: Boolean) {
        _isOnline.value = isOnline
    }
}