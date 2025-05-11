package com.example.myproject.Repository

import android.util.Log
import com.example.myproject.Model.OldTopic
import com.example.myproject.Model.Topic
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TopicRepository {
    private val db = FirebaseDatabase.getInstance()
    private var topicsRef = db.getReference("topics")
    private var mAuth = FirebaseAuth.getInstance()

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
//
                var list = ArrayList<Topic>()

                var snapshotTheme = topicsRef.get().await()


                for(item in snapshotTheme.children){
                    val key:String = item.key.toString()
                    var snapshotTopic = topicsRef.child(key).get().await()
                     for(item in snapshotTopic.children){
                        var topic = item.getValue(Topic::class.java)
                        if(topic!=null){
                            list.add(topic)
                        }
                     }

            }
                list
        } catch (e: Exception){
               Log.e("TopicRepository", "loi ${e}")
                mutableListOf<Topic>()
        }

        }
    }

    suspend fun getOldTopics(): MutableList<Topic>{
        return withContext(Dispatchers.IO){
            try {
                val UID = mAuth.currentUser?.uid.toString()
                var snapshot = db.getReference("users").child(UID).child("listTopicStuded").get().await()
                var list = ArrayList<Topic>()
                for(item in snapshot.children){
                    var oldtopic = item.getValue(OldTopic::class.java)
                    if(oldtopic!=null){
                        var topic = getTopicByID(oldtopic.id)
                        topic?.let {
                            list.add(it)
                        }

                    }
                }

                list
            } catch (e: Exception){
                Log.e("TopicRepository", "loi ${e}")
                mutableListOf<Topic>()
            }

        }
    }
    suspend fun getTopicByTheme(id:String): MutableList<Topic>{
        return withContext(Dispatchers.IO){
            try {
                var list = ArrayList<Topic>()
                var snapshot = topicsRef.child(id).get().await()
                for(item in snapshot.children) {
                    var topic = item.getValue(Topic::class.java)
                    if (topic != null) {
                        list.add(topic)
                    }
                }
                list
            }catch (e:Exception){
               Log.d("TopicRepository", "loi ${e}")
                mutableListOf<Topic>()
            }
        }

    }
    suspend fun getTheme(): MutableList<String>{
        return withContext(Dispatchers.IO){
            try {
                var list = ArrayList<String>()
                var snapshot = topicsRef.get().await()
                for(item in snapshot.children){
                    var key = item.key.toString()
                    list.add(key)
                }
                list
            } catch (e:Exception){
                Log.d("loigetTheme","${e}")
                mutableListOf<String>()
            }
        }
    }
//    suspend fun getTopicByID(id:String): Topic? {
//         return withContext(Dispatchers.IO){
//            try {
//                var  topic = Topic() // tạo topic rỗng
//
//                var snapshot = topicsRef.child(id).get().await()
//
//                  var Ntopic = snapshot.getValue(Topic::class.java)!!
//                   if (Ntopic != null) {
//                       topic = Ntopic
//                   }
//                topic
//               }
//                 catch (e: Exception){
//                Log.e("TopicRepository", "loi ${e}")
//                 null
//            }
//        }
//    }
    suspend fun getTopicByID(id: String): Topic? = withContext(Dispatchers.IO) {
        return@withContext try {
            val snapshot = topicsRef.child(id).get().await()
            snapshot.getValue(Topic::class.java)
        } catch (e: Exception) {
            Log.e("TopicRepository", "Lỗi khi lấy topic: ${e.message}")
            null
        }
    }


}