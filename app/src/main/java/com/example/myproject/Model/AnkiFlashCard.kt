package com.example.myproject.Model

class AnkiFlashCard( val id: Int = 1,
                     val tuvung: String = "",
                     val meaning: String = "",
                     val romaji: String = "",
                     val exampleJapanese: String = "",
                     var status: Int = 1) {
    constructor(): this(1,"","","","",1)
}