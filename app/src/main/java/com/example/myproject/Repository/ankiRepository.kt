package com.example.myproject.Repository

import com.example.myproject.Model.Anki
import com.example.myproject.Model.AnkiFlashCard
import com.example.myproject.Model.FlashcardModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ankiRepository {
    private val db = FirebaseDatabase.getInstance()
//    private var topicsRef = db.getReference("topics")
    private var mAuth = FirebaseAuth.getInstance()

   suspend fun pushNameSetIntoAnki(name:String){
        val ankiref = db.getReference("anki")
        val userID = mAuth.currentUser?.uid.toString()
       var demoAnki = Anki()
        ankiref.child(userID).child(name).setValue("")
    }
    suspend fun pushflashcardIntoAnki(name:String,flashcardModel: AnkiFlashCard){
        val ankiref = db.getReference("anki")
        val userID = mAuth.currentUser?.uid.toString()
        var demoAnki = this.getAnki()
        var listFlashcard: MutableList<AnkiFlashCard> = demoAnki.ankiflashcard
        listFlashcard.add(flashcardModel)
        var map = mutableMapOf<String,Any>(
            "ankiflashcard" to listFlashcard
        )
        ankiref.child(userID).child(name).updateChildren(map)
    }
    suspend fun getNameAnki() :MutableList<String>{
        return withContext(Dispatchers.IO) {
            try {
                var list = mutableListOf<String>()
                val ankiref = db.getReference("anki")
                val userID = mAuth.currentUser?.uid.toString()
                val snapshot = ankiref.child(userID).get().await()
                for (item in snapshot.children) {
                    val key = item.key.toString()
                    if (key != null) {
                        list.add(key)

                    }
                }
                list
            } catch (e: Exception) {
                mutableListOf<String>()
            }
        }
    }
    suspend fun getAnki(): Anki {
        return withContext(Dispatchers.IO) {
            try {
                var list = Anki()
                val ankiref = db.getReference("anki")
                val userID = mAuth.currentUser?.uid.toString()
                val snapshot = ankiref.child(userID).get().await()
                for (item in snapshot.children) {
                    val anki = item.getValue(Anki::class.java)
                    if (anki != null) {
                        list = anki

                    }
                }
                list
            } catch (e: Exception) {
              Anki()
            }
        }
    }

}