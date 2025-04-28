package com.example.myproject.Repository

import android.util.Log
import com.example.myproject.Model.Topic
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class TopicRepository {
    private val db = FirebaseDatabase.getInstance()
    private var topicsRef = db.getReference("topics")
fun pushTopic(topic:Topic, onSuccess:()->Unit , onFailure:(Exception)->Unit ){
 var newTopicRef = topicsRef.push()
    newTopicRef.setValue(topic)
        .addOnSuccessListener {
            Log.d("TopicRepository", "Push Topic thành công")
            onSuccess()
        }
        .addOnFailureListener{ expection ->
            Log.e("TopicRepository", "Push Topic thất bại $expection ")
            onFailure(expection)
        }
}
}