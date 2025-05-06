package com.example.myproject.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.Model.Topic
import com.example.myproject.Model.User
import com.example.myproject.Repository.AuthRepository
import com.example.myproject.Repository.TopicRepository
import kotlinx.coroutines.launch
import okhttp3.Callback

class HomeViewmodel: ViewModel() {
    private val topicRepository = TopicRepository()
    private val _topics =  MutableLiveData<List<Topic>>()
    val topics: LiveData<List<Topic>> get() = _topics
    private val authReporitory = AuthRepository()

    fun fetchTopics(){
        viewModelScope.launch {
try {
    val listTopics = topicRepository.getTopics()
    _topics.postValue(listTopics)
}catch (e: Exception){
    throw e
    Log.e("HomeViewmodel", "loi ${e}")
}
        }
    }
    fun getUser(callback: (User?) -> Unit){
        authReporitory.getUserByID(callback)
    }
  fun updateUserByID(userId:String, map: Map<String, Any>){
      authReporitory.updateUserByID(userId, map)
  }

}