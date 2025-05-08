package com.example.myproject.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.Model.OldTopic
import com.example.myproject.Model.Topic
import com.example.myproject.Repository.AuthRepository
import com.example.myproject.Repository.TopicRepository
import kotlinx.coroutines.launch

class LearVocabularyViewModel: ViewModel() {
    private var _topics = MutableLiveData<List<Topic>>()
    val topics: LiveData<List<Topic>> get() = _topics
    private var _ListIdOldTopic = MutableLiveData<List<String>>()
    val ListIdOldTopic: LiveData<List<String>> get() = _ListIdOldTopic
    private val topicRepository = TopicRepository()
    private val authRepository = AuthRepository()
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
    }

    fun getTopicByposition(position: Int): Topic? {
        return _topics.value?.get(position)
    }

    fun getListIdOldTopic() {
        viewModelScope.launch {
            try {
                val listIdTopics = authRepository.getOldTopicsFromUser()
                _ListIdOldTopic.postValue(listIdTopics)
            } catch (e: Exception) {

            }
        }

    }

    fun changeListIdOldTopic(id: String) {
        val currentList = _ListIdOldTopic.value?.toMutableList() ?: mutableListOf()

        // Nếu đã tồn tại id trong danh sách, xóa nó để tránh trùng lặp
        currentList.remove(id)

        // Nếu danh sách đã đủ 3 phần tử, xóa phần tử cuối cùng trước
        if (currentList.size >= 3) {
            currentList.removeAt(currentList.lastIndex)
        }

        // Thêm id mới vào đầu danh sách
        currentList.add(0, id)
          Log.d("test", "${currentList.size}")
        _ListIdOldTopic.postValue(currentList)
    }

    fun updateUser(){
        var listTopic =  ArrayList<OldTopic>()
        _ListIdOldTopic.value?.forEach { id ->
             var OldTopic = OldTopic(id)
            listTopic.add(OldTopic)
        }

        var map = mutableMapOf<String, Any>(
            "listTopicStuded" to listTopic
        )

        authRepository.updateUserByID(map)

    }
}