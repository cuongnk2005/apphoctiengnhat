package com.example.myproject.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myproject.Repository.AuthRepository

class LoginViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    fun login(strEmail: String, strMk:String) : LiveData<String> {
        Log.d("tesstham", "da chay ham nay")
        val result =  authRepository.login(strEmail,strMk)
        return result
    }
}