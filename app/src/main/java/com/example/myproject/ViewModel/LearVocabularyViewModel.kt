package com.example.myproject.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.Model.OldTopic
import com.example.myproject.Model.Topic
import com.example.myproject.Model.User
import com.example.myproject.Repository.AuthRepository
import com.example.myproject.Repository.TopicRepository
import kotlinx.coroutines.launch

class LearVocabularyViewModel: ViewModel() {
    private var _topics = MutableLiveData<List<Topic>>()
    val topics: LiveData<List<Topic>> get() = _topics
    private var _ListOldTopic = MutableLiveData<List<OldTopic>>()
    val ListOldTopic: LiveData<List<OldTopic>> get() = _ListOldTopic
    private var _theme = MutableLiveData<List<String>>()
    val theme: LiveData<List<String>> get() = _theme
    private val topicRepository = TopicRepository()
    private val authRepository = AuthRepository()
    private var oldTopic = OldTopic()
    private val userInstance = User()
    fun fetchTopics() {
        viewModelScope.launch {
            try {
                val listTopics = topicRepository.getTopics()
                _topics.postValue(listTopics)
            } catch (e: Exception) {
                throw e
                Log.e("HomeViewmodel", "loi ${e}")
            }
        }
        // Lưu lại danh sách đầy đủ

    }

    // Phương thức lọc topics theo category
    fun fetchTopicsByTheme(theme: String) {
        viewModelScope.launch {
            try {
                val listTopics = topicRepository.getTopicByTheme(theme)
                _topics.postValue(listTopics)
            } catch (e: Exception) {
                throw e
                Log.e("HomeViewmodel", "loi ${e}")
            }
        }

    }

    // Các phương thức hiện tại của bạn
    fun getTopicByposition(position: Int): Topic? {
        return topics.value?.getOrNull(position)
    }

    fun getListIdOldTopic() {
        viewModelScope.launch {
            try {
                val listIdTopics = authRepository.getOldTopicsFromUser()
                _ListOldTopic.postValue(listIdTopics)
            } catch (e: Exception) {

            }
        }

    }

    fun changeListOldTopic(id: String) {
        var currentList = _ListOldTopic.value?.toMutableList() ?: mutableListOf()
        currentList =  oldTopic.changeListOldTopic(id, currentList)
        _ListOldTopic.postValue(currentList)
    }

    fun updateUser() {
        userInstance.updateUser(_ListOldTopic)

    }

    fun getTheme() {
        viewModelScope.launch {
            try {
                val listTheme = topicRepository.getTheme()
                listTheme.add(0, "All")
               _theme.postValue(listTheme)
            } catch (e: Exception) {
                throw e
            }
        }


    }
}