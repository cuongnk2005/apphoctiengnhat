package com.example.myproject.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myproject.Repository.AuthRepository

class LoginViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    fun login(strEmail: String, strMk:String) : LiveData<String> {
        return authRepository.login(strEmail,strMk)
    }
}