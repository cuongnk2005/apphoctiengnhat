package com.example.myproject.Repository

import com.google.firebase.firestore.FirebaseFirestore

class TopicRepository {
    private val db = FirebaseFirestore.getInstance()
    private var topicCollection = db.collection("Topic")

}