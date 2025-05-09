package com.example.myproject.Model

import java.io.Serializable

class OldTopic(var id:String = "") :Serializable {
    constructor(): this(id = "")

    override fun toString(): String {
        return "OldTopic(id='$id')"

    }
}