package com.example.myproject.Model

import java.io.Serializable

class User(var gmail: String = "", var password: String = "", var username:String = "",
           var url:String = "", var listTopicStuded:ArrayList<OldTopic>  ) :Serializable {
    constructor():this("","","","", ArrayList())
    override fun toString(): String {
        return "User(gmail='$gmail', password='$password', username='$username', url='$url', listTopicStuded=$listTopicStuded)"

    }
}