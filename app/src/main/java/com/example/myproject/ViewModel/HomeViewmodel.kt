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

class HomeViewmodel: ViewModel() {
    private val topicRepository = TopicRepository()
    private val _topics =  MutableLiveData<List<Topic>>()
    val topics: LiveData<List<Topic>> get() = _topics
    private val _user =  MutableLiveData<User>()
    val user: LiveData<User> get() = _user
    private val authReporitory = AuthRepository()
    private val _progressText = MutableLiveData<String>()
    val progressText: LiveData<String> get() = _progressText

    private val _progressPercentage = MutableLiveData<Int>()
    val progressPercentage: LiveData<Int> = _progressPercentage

    fun fetchTopics(){
        viewModelScope.launch {
            try {
                val listTopics = topicRepository.getOldTopicsByHome()
                _topics.postValue(listTopics)
            }catch (e: Exception){
                throw e
                Log.e("HomeViewmodel", "loi ${e}")
            }
        }
    }
    fun getUser(callback: (User?) -> Unit){
        try {
            authReporitory.getUserByID(callback)
        } catch (e: Exception){

            Log.e("debugdetUser", "loi ${e}")
        }

    }

    fun setUserData(user: User) {
        _user.value = user
    }
    fun getUserInViewModel(): User? {
        return _user.value
    }
    fun getTopicByposition(position: Int): Topic?{
        return _topics.value?.get(position)
    }
    fun updatepercen(){
        viewModelScope.launch {
            val ListTopic = topicRepository.getTopics()
            val total = ListTopic.size
            authReporitory.getUserByID {
                if (it != null) {
                    var leanerd =   it.listTopicStuded.count { it.status == true }
                    var percent = (leanerd*100)/total
                    _progressText.postValue("Tiến độ học tập: ${percent}%")
                    _progressPercentage.postValue((leanerd*100)/total)
                }

            }
        }

    }

}