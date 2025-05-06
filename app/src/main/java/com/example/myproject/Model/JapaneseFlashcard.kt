package com.example.myproject.Model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class JapaneseFlashcard(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Mặt trước thẻ
    val kanji: String,          // Chữ Kanji
    val hiragana: String,       // Phiên âm hiragana
    val romaji: String,         // Phiên âm latin

    // Mặt sau thẻ
    val meaning: String,        // Nghĩa tiếng Việt
    val definition: String,     // Định nghĩa chi tiết
    val example: String,        // Ví dụ câu tiếng Nhật
    val exampleReading: String, // Phiên âm của ví dụ
    val exampleMeaning: String, // Nghĩa của ví dụ

    // Tài nguyên bổ sung
    val imageUrl: String? = null,       // Đường dẫn hình ảnh minh họa
    val audioUrl: String? = null,       // Đường dẫn file phát âm

    // Trạng thái học
    val level: Int = 0,                // Mức độ thành thạo (0-5)
    val nextReviewTime: Long = 0,      // Thời gian ôn tập lần tiếp theo
    val reviewCount: Int = 0,          // Số lần đã ôn tập

    // Phân loại
    val deckId: Long,                  // ID của bộ thẻ chứa flashcard này
    val tags: String = ""              // Các thẻ tag (phân cách bằng dấu phẩy)
)