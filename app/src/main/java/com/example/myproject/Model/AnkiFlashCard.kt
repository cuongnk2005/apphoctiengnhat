package com.example.myproject.Model

import java.time.LocalDate

class AnkiFlashCard( val id: Int = 1,
                     val tuvung: String = "",
                     val meaning: String = "",
                     val romaji: String = "",
                     val exampleJapanese: String = "",
                     var easiness: Double = 2.5,
                     var repetition: Int = 0,
                     var interval: Int = 0,
                     var nextReviewDate: LocalDate = LocalDate.now()) {

    constructor(): this(1,"","","","",2.5,0,0,LocalDate.now())

//    fun  updateFlashCard(card: AnkiFlashCard, quality: Int){
//        if(quality < 3){
//            card.repetition = 0
//            card.interval = 0
//        } else {
//            card.repetition += 1
//            if(card.repetition == 1) card.interval = 1
//            else if(card.repetition == 2) card.interval = 2
//            else card.interval = (card.interval * card.easiness).toInt()
//            card.easiness = card.easiness + 0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02)
//            if (card.easiness < 1.3) card.easiness = 1.3
//
//
//        }
//
//    }
    fun  updateFlashCard(quality: Int){
        if(quality < 3){
            this.repetition = 0
            this.interval = 0
        } else {
            this.repetition += 1
            if(this.repetition == 1) this.interval = 1
            else if(this.repetition == 2) this.interval = 2
            else this.interval = (this.interval * this.easiness).toInt()
            this.easiness = this.easiness + 0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02)
            if (this.easiness < 1.3) this.easiness = 1.3


        }

    }
}