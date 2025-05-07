package com.example.myproject.Model


data class DictionaryEntry(
    val id: Int = 0,
    val word: String,         // Từ (tiếng Nhật)
    val reading: String,      // Cách đọc
    val meaning: String,      // Nghĩa (tiếng Việt)
    val wordDecribe : String,
    val wordExample : String,
    val wordMean:String,
    val wordType: String = "Danh từ",  // Loại từ
    var isFavorite: Boolean = false    // Trạng thái yêu thích
)

data class Example(
    val original: String,    // Câu ví dụ tiếng Nhật
    val translated: String   // Câu ví dụ tiếng Việt
)