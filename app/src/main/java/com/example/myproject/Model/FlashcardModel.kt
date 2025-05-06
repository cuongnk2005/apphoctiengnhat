package com.example.myproject.Model

data class FlashcardModel(
    val id: Int,
    val kanji: String,
    val hiragana: String,
    val romaji: String,
    val meaning: String,
    val definition: String,
    val exampleJapanese: String,
    val exampleReading: String,
    val exampleMeaning: String,
    var isKnown: Boolean = false
)

data class FlashcardSetInfo(
    val id: Int,
    val title: String,
    val totalCards: Int,
    val learnedCards: Int,
    val level: String,
    val category: String
)