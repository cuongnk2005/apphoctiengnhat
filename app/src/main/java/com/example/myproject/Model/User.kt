package com.example.myproject.Model

import androidx.lifecycle.MutableLiveData
import com.example.myproject.Repository.AuthRepository
import java.io.Serializable
private val authRepository = AuthRepository()
class User(var gmail: String = "", var password: String = "", var username:String = "",
           var url:String = "", var listTopicStuded:ArrayList<OldTopic>  ) :Serializable {


    constructor():this("","","","", ArrayList())
    override fun toString(): String {
        return "User(gmail='$gmail', password='$password', username='$username', url='$url', listTopicStuded=$listTopicStuded)"

    }
    fun updateUser(mutable: MutableLiveData<List<OldTopic>>) {

        var listTopic = ArrayList<OldTopic>()
        mutable.value?.forEach { old ->
            var result = OldTopic(old.id,old.status)
            listTopic.add(result)
        }

        var map = mutableMapOf<String, Any>(
            "listTopicStuded" to listTopic
        )
        authRepository.updateUserByID(map)

    }
}