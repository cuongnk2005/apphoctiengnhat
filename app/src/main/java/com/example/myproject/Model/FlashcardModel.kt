package com.example.myproject.Model

data class FlashcardModel(
    val id: Int = 1,
    val kanji: String = "",
    val hiragana: String = "",
    val romaji: String = "",
    val meaning: String = "",
    val definition: String = "",
    val exampleJapanese: String = "",
    val exampleReading: String = "",
    val exampleMeaning: String = "",
    var isKnown: Boolean = false

) {
    constructor() : this(
        1, "", "", "", "",  "", "",
        "", "", false
    )
}

data class FlashcardSetInfo(
    val id: Int,
    val title: String,
    val totalCards: Int,
    val learnedCards: Int,
    val level: String,
    val category: String,
    val image: String = ""
)

// Status for tracking flashcard learning state
enum class FlashcardStatus {
    NEW,        // Not studied yet
    LEARNING,   // Currently being learned
    KNOWN,      // Marked as known
    REVIEW      // Needs review
}

// User progress for a specific flashcard
data class UserFlashcardProgress(
    val flashcardId: Int,
    var status: FlashcardStatus = FlashcardStatus.NEW,
    var reviewCount: Int = 0,
    var lastReviewed: Long = 0,  // Timestamp
    var nextReviewDue: Long = 0, // Timestamp for next review based on SRS
    var accuracyRate: Float = 0f // Accuracy rate in quizzes
)