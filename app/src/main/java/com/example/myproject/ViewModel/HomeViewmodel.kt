package com.example.myproject.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myproject.Model.Topic
import com.example.myproject.Model.Vocabulary
import com.example.myproject.Repository.AuthRepository

class HomeViewmodel: ViewModel() {
    private var listtvs : ArrayList<Vocabulary> = ArrayList()
    private  val _studentList = MutableLiveData<List<Topic>>().apply {
        value = listOf(
            Topic("1", "Nguyen Van A",listtvs),
            Topic("2", "Tran Thi B",listtvs),
            Topic("3", "Le Thi C",listtvs),
            Topic("1", "Nguyen Van A",listtvs),
            Topic("2", "Tran Thi B",listtvs),
            Topic("1", "Nguyen Van A",listtvs),
            Topic("2", "Tran Thi B",listtvs),
            Topic("3", "Le Thi C",listtvs),
            Topic("1", "Nguyen Van A",listtvs),
            Topic("2", "Tran Thi B",listtvs)

        ) // Hoặc có thể là một danh sách sinh viên mặc định
    }
    val studentList: LiveData<List<Topic>> = _studentList
}