package com.example.myproject.Model

data class JishoResponse(
    val data: List<JishoEntry>
)

data class JishoEntry(
    val slug: String,
    val japanese: List<Japanese>,
    val senses: List<Sense>
)

data class Japanese(
    val word: String?,
    val reading: String
)

data class Sense(
    val english_definitions: List<String>,
    val parts_of_speech: List<String>,
    val tags: List<String> = emptyList(),
    val info: List<String> = emptyList()
)

// Model DictionaryEntry đã có mà chúng ta giữ nguyên
data class DictionaryEntry(
    val id: Int,
    val word: String,        // Từ tiếng Nhật
    val reading: String,     // Cách đọc
    val meaning: String,     // Nghĩa chính
    val explanation: String, // Giải thích chi tiết
    val example: String,     // Ví dụ
    val exampleMeaning: String, // Nghĩa của ví dụ
    val partOfSpeech: String    // Loại từ
)

// Hàm mở rộng để chuyển đổi từ JishoEntry sang DictionaryEntry
fun JishoEntry.toDictionaryEntry(id: Int): DictionaryEntry {
    val word = japanese.firstOrNull()?.word ?: slug
    val reading = japanese.firstOrNull()?.reading ?: ""

    // Lấy nghĩa tiếng Anh đầu tiên làm nghĩa chính
    val meaning = senses.firstOrNull()?.english_definitions?.joinToString(", ") ?: ""

    // Tạo phần giải thích chi tiết kết hợp cả nghĩa và thông tin bổ sung
    val explanation = buildString {
        senses.forEach { sense ->
            // Thêm các định nghĩa
            append(sense.english_definitions.joinToString(", "))

            // Thêm thông tin bổ sung nếu có
            if (sense.info.isNotEmpty()) {
                append(" (")
                append(sense.info.joinToString(", "))
                append(")")
            }

            // Thêm thẻ nếu có
            if (sense.tags.isNotEmpty()) {
                append(" [")
                append(sense.tags.joinToString(", "))
                append("]")
            }

            append("\n")
        }
    }.trim()

    // API Jisho không có ví dụ và nghĩa của ví dụ, nên để trống
    val example = ""
    val exampleMeaning = ""

    // Lấy loại từ
    val partOfSpeech = senses.firstOrNull()?.parts_of_speech?.firstOrNull() ?: ""

    return DictionaryEntry(
        id = id,
        word = word,
        reading = reading,
        meaning = meaning,
        explanation = explanation,
        example = example,
        exampleMeaning = exampleMeaning,
        partOfSpeech = partOfSpeech
    )
}