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

data class DictionaryEntry(
    val id: Int,
    val word: String,
    val reading: String,
    val meaning: String,
    val decribe: String,
    val example: String,
    val exampleMeaning: String,
    val wordType: String
)

fun JishoEntry.toDictionaryEntry(id: Int): DictionaryEntry {
    val word = japanese.firstOrNull()?.word ?: slug
    val reading = japanese.firstOrNull()?.reading ?: ""

    val meaning = senses.firstOrNull()?.english_definitions?.joinToString(", ") ?: ""

    // gop nghia vs thoong tin thanh giai thich
    val explanation = buildString {
        senses.forEach { sense ->

            append(sense.english_definitions.joinToString(", "))

            if (sense.info.isNotEmpty()) {
                append(" (")
                append(sense.info.joinToString(", "))
                append(")")
            }

            if (sense.tags.isNotEmpty()) {
                append(" [")
                append(sense.tags.joinToString(", "))
                append("]")
            }

            append("\n")
        }
    }.trim()

    val example = ""
    val exampleMeaning = ""

    val partOfSpeech = senses.firstOrNull()?.parts_of_speech?.firstOrNull() ?: ""

    return DictionaryEntry(
        id = id,
        word = word,
        reading = reading,
        meaning = meaning,
        decribe = explanation,
        example = example,
        exampleMeaning = exampleMeaning,
        wordType = partOfSpeech
    )
}