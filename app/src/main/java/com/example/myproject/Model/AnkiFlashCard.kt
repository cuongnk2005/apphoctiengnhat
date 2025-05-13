package com.example.myproject.Model

class AnkiFlashCard( val id: Int = 1,
                     val tuvung: String = "",
                     val meaning: String = "",
                     val exampleJapanese: String = "",
                     val romaji: String = "",
                     var nextReviewTime: Long = 0,
                     var status: Int = 1) {
    constructor(): this(1,"","","","",0, 1)
}