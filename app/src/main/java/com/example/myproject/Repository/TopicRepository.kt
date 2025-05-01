package com.example.myproject.Repository

import android.util.Log
import com.example.myproject.Model.Topic
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TopicRepository {
    private val db = FirebaseDatabase.getInstance()
    private var topicsRef = db.getReference("topics")
fun pushTopic(topic:Topic, onSuccess:()->Unit , onFailure:(Exception)->Unit ){
    var newTopicRef = topicsRef.push()
    newTopicRef.setValue(topic)
        .addOnSuccessListener {
            Log.d("TopicRepositoryy", "Push Topic thành công")
            onSuccess()
        }
        .addOnFailureListener{ expection ->
            Log.e("TopicRepositoryyy", "Push Topic thất bại $expection ")
            onFailure(expection)
        }
}
//    fun getTopics(onSuccess: (List<Topic>) -> Unit, onFailure: (DatabaseError) -> Unit ){
//  topicsRef.addListenerForSingleValueEvent(object : ValueEventListener{
//      override fun onDataChange(snapshot: DataSnapshot) {
//          var list = ArrayList<Topic>()
//         for(item in snapshot.children){
//             var topic = item.getValue(Topic::class.java)
//             if(topic!= null){
//                 list.add(topic)
//             }
//         }
//          onSuccess(list)
//
//      }
//
//      override fun onCancelled(error: DatabaseError) {
//          onFailure(error)
//      }
//
//  })
//    }
    suspend fun getTopics(): MutableList<Topic>{
        return withContext(Dispatchers.IO){
            try {
                var snapshot = topicsRef.get().await()
                var list = ArrayList<Topic>()
                for(item in snapshot.children){
                    var topic = item.getValue(Topic::class.java)
                    if(topic!=null){
                        list.add(topic)
                    }
            }
                list
        } catch (e: Exception){
               Log.e("TopicRepository", "loi ${e}")
                mutableListOf<Topic>()
        }

        }
    }
}