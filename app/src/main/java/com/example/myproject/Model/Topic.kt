package com.example.myproject.Model

import Vocabulary

class Topic(
    var id:String = "",
    var NameTopic: String = "",
    var description: String = "",
    var vocabulary_list: ArrayList<FlashcardModel> = ArrayList()
) {
    // Constructor không tham số (Firebase yêu cầu)
    constructor() : this("","", "", ArrayList())

    override fun toString(): String {
        return "Topic( NameTopic='$NameTopic', description='$description', vocabulary_list=$vocabulary_list)"
    }
}
