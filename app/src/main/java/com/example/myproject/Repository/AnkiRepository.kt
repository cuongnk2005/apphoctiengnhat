package com.example.myproject.Repository

import android.util.Log
import com.example.myproject.Model.Anki
import com.example.myproject.Model.AnkiFlashCard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import kotlin.math.log


class AnkiRepository {
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

        var listFlashcard: MutableList<AnkiFlashCard> = getAnkiFlasCardByName(name)

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
                    if (key.isNotEmpty()) {
                        list.add(key)

                    }
                }
                list
            } catch (e: Exception) {
                mutableListOf<String>()
            }
        }
    }
    //    suspend fun getAnki(): Anki {
//        return withContext(Dispatchers.IO) {
//            try {
//                var list = Anki()
//                val ankiref = db.getReference("anki")
//                val userID = mAuth.currentUser?.uid.toString()
//                val snapshot = ankiref.child(userID).child().get().await()
//                for (item in snapshot.children) {
//                    val anki = item.getValue(Anki::class.java)
//                    if (anki != null) {
//                        list = anki
//                    }
//                }
//                list
//            } catch (e: Exception) {
//              Anki()
//            }
//        }
//    }
    suspend fun getAnkiFlasCardByName(name: String): MutableList<AnkiFlashCard> {
        return withContext(Dispatchers.IO) {
            try {
                var list = mutableListOf<AnkiFlashCard>()
                val userID = mAuth.currentUser?.uid.toString()
                val snapshot = db.getReference("anki").child(userID).child(name).child("ankiflashcard").get().await()
                for (item in snapshot.children) {
                    val ankiFlashCard = item.getValue(AnkiFlashCard::class.java)
                    if (ankiFlashCard != null) {
                        ankiFlashCard.id = item.key!!.toInt()
                        list.add(ankiFlashCard)
                    }
                }
                Log.d("listFlashcard","${list.size}")
                list
            } catch (e: Exception) {
                Log.d("ksjkfs","$e")
                mutableListOf<AnkiFlashCard>()
            }
        }
    }
    suspend fun getAnkiFlasCardByNameForLear(name: String): MutableList<AnkiFlashCard> {
        return withContext(Dispatchers.IO) {
            try {
                var list = mutableListOf<AnkiFlashCard>()
                val userID = mAuth.currentUser?.uid.toString()
                val snapshot = db.getReference("anki").child(userID).child(name).child("ankiflashcard").get().await()
                for (item in snapshot.children) {
                    val ankiFlashCard = item.getValue(AnkiFlashCard::class.java)
                    if (ankiFlashCard != null) {
                        ankiFlashCard.id = item.key!!.toInt()
                        val now = LocalDateTime.now()
                        val ankiTime = LocalDateTime.parse(ankiFlashCard.nextReviewDate)
                        if(ankiTime.isBefore(now)){
                            list.add(ankiFlashCard)
                        }

                    }
                }
                Log.d("listFlashcard","${list.size}")
                list
            } catch (e: Exception) {
                Log.d("ksjkfs","$e")
                mutableListOf<AnkiFlashCard>()
            }
        }
    }
    fun UpdateAnkiFlasCardByName(name: String, card: AnkiFlashCard) {
        try {
            var list = mutableListOf<AnkiFlashCard>()
            val userID = mAuth.currentUser?.uid.toString()
            val referent = db.getReference("anki").child(userID).child(name).child("ankiflashcard").child(card.id.toString()).setValue(card)
                .addOnSuccessListener {
                    Log.d("sucessUpdateCard", "them thanh cong")
                }
                .addOnFailureListener{ error ->
                    Log.e("errorUpdateCard", "loi ${error}")
                }
        } catch (e: Exception) {

        }
    }

    fun deleteAnki(name: String) {
        try {
            val userID = mAuth.currentUser?.uid.toString()
            val referent = db.getReference("anki").child(userID).child(name).removeValue()
                .addOnSuccessListener {
                    Log.d("deleteAnki", "xoa thanh cong")
                }
                .addOnFailureListener { error ->
                    Log.e("deleteAnki", "loi ${error}")
                }
        } catch (e:Exception) {
            Log.e("deleteAnki", "SDFS")
        }
    }

    fun deleteAnkiFlashCard(id:Int, name:String){
        val nodeRef = db.getReference("anki").child(mAuth.currentUser?.uid.toString()).child(name).child(id.toString())
        nodeRef.removeValue()
            .addOnSuccessListener { aVoid: Void? ->
                Log.d("Firebase", "Node đã được xóa")
            }
            .addOnFailureListener { e: java.lang.Exception? ->
                Log.e("Firebase", "Lỗi khi xóa node", e)
            }
    }

}