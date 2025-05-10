package com.example.myproject.Model

class Topic(
    var id: String = "",
    var NameTopic: String = "",
    var description: String = "",
    var imageUrl: String = "",
    var level: String = "N5",

    var vocabulary_list: ArrayList<FlashcardModel> = ArrayList()
) {
    // Constructor không tham số (Firebase yêu cầu)
    constructor() : this("", "", "", "", "N5", ArrayList())

    override fun toString(): String {
        return "Topic(NameTopic='$NameTopic', description='$description', level='$level', vocabulary_list=$vocabulary_list)"
    }
}
