package com.example.myproject.Model

import Vocabulary

class Topic(
    var id: String = "",
    var NameTopic: String = "",
    var description: String = "",
    var imageUrl: String = "",
    var level: String = "N5",
    var tags: List<String> = listOf(),
    var wordCount: Int = 0,
    var vocabulary_list: ArrayList<FlashcardModel> = ArrayList()
) {
    // Constructor không tham số (Firebase yêu cầu)
    constructor() : this("", "", "", "", "N5", listOf(), 0, ArrayList())

    override fun toString(): String {
        return "Topic(NameTopic='$NameTopic', description='$description', level='$level', wordCount=$wordCount, vocabulary_list=$vocabulary_list)"
    }
}
