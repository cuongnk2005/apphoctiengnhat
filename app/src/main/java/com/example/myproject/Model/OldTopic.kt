package com.example.myproject.Model

import android.util.Log
import java.io.Serializable

class OldTopic(var id:String = "", var status:Boolean = false ) :Serializable {
    constructor(): this(id = "", status = false)

    override fun toString(): String {
        return "OldTopic(id='$id', status=$status)"
    }
    fun changeListOldTopic(id: String, multitable: MutableList<OldTopic>):MutableList<OldTopic> {

        val foundTopic:OldTopic? = multitable.find { it.id == id }
        if (foundTopic != null) {
            multitable.removeIf {
                it.id == id }
            var newOldTopic = OldTopic(id, foundTopic.status)
            multitable.add(0, newOldTopic)
        } else {
            var newOldTopic = OldTopic(id, false)
            multitable.add(0, newOldTopic)
        }


        return multitable
    }
    fun changeListOldTopic(id: String, multitable: MutableList<OldTopic>, status: Boolean):MutableList<OldTopic> {
            multitable.removeIf {
                it.id == id }
            var newOldTopic = OldTopic(id, status)
            multitable.add(0, newOldTopic)
        return multitable
    }
}