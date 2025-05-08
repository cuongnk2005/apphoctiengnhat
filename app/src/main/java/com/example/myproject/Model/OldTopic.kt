package com.example.myproject.Model

class OldTopic(var id:String = "")  {
    constructor(): this(id = "")

    override fun toString(): String {
        return "OldTopic(id='$id')"

    }
}