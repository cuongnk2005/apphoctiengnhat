package com.example.myproject.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myproject.Repository.DictionaryRepository

class DictionaryViewModelFactory(private val repository: DictionaryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DictionaryViewModel::class.java)) {
            @Suppress("continue")
            return DictionaryViewModel(repository) as T
        }
        throw IllegalArgumentException("loi kh phai viewmodel")
    }
}