package com.example.myproject.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myproject.Repository.AuthRepository

class RegisterViewModel: ViewModel() {
    private val authRepository = AuthRepository()
    fun register(strEmail: String, strName: String, strMk: String): LiveData<String> {
        Log.d("tesstham", "da chay ham nay")
        val result = authRepository.register(strEmail, strName, strMk)
        return result
    }
}