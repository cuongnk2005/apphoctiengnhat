package com.example.myproject.Model

import java.io.Serializable

class User(var gmail: String = "", var password: String = "", var username:String = "", var url:String = ""  ) :Serializable {
    constructor():this("","","","")
}