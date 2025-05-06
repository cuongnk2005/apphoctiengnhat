package com.example.myproject.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myproject.Model.User
import com.example.myproject.Repository.AuthRepository

class ProfileModel: ViewModel() {
    private val authReporitory = AuthRepository()
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user
    fun setUserData(user: User) {
        _user.value = user
    }
    fun updateUserByID( map: Map<String, Any>){
        authReporitory.updateUserByID(map)
    }
}