package com.example.myproject.Model

import Vocabulary

class Topic(
    var theme: String = "",
    var NameTopic: String = "",
    var description: String = "",
    var vocabulary_list: ArrayList<Vocabulary> = ArrayList()
) {
    // Constructor không tham số (Firebase yêu cầu)
    constructor() : this("","", "", ArrayList())
}
