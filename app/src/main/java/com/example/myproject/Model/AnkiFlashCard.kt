package com.example.myproject.Model

import org.checkerframework.common.returnsreceiver.qual.This
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.max
import kotlin.math.min

enum class CardState {
    NEW, LEARNING, REVIEW, RELEARNING
}

data class AnkiFlashCard( var id: Int = 10,
                          val tuvung: String = "",
                          val meaning: String = "",
                          val exampleJapanese: String = "",
                          var romaji: String = "",
                          var state : CardState = CardState.NEW,
                          var easeFactor: Double = 2.5,
                          var repetitions: Int = 0,
                          var learningStep: Int = 0,
                          var interval: Int = 0,
                          var nextReviewDate:String = LocalDateTime.now().toString()) {

    constructor(): this(1,"","","","",CardState.NEW,2.5,0,0,0,LocalDateTime.now().toString())

    override fun toString(): String {
        return "AnkiFlashCard(id=$id, tuvung='$tuvung', meaning='$meaning', exampleJapanese='$exampleJapanese', romaji='$romaji', easiness=$easeFactor, repetition=$repetitions, interval=$interval, nextReviewDate=$nextReviewDate)"

    }
}
class AnkiScheduler(
    private val learningSteps: List<Int> = listOf(1, 10), // Bước học (phút)
    private val graduatingInterval: Int = 1, // Khoảng thời gian sau khi hoàn thành học (ngày)
    private val easyBonus: Double = 1.3, // Hệ số thưởng khi chọn Easy
    private val hardInterval: Double = 1.2, // Hệ số khi chọn Hard
    private val intervalModifier: Double = 1.0, // Hệ số điều chỉnh khoảng thời gian
    private val minimumInterval: Int = 1, // Khoảng thời gian tối thiểu (ngày)
    private val relearnMultiplier: Double = 0.1 // Hệ số khoảng thời gian khi học lại
) {
    // Cập nhật trạng thái thẻ sau khi ôn tập
    fun reviewCard(
        card: AnkiFlashCard,
        quality: Int,
        currentTime: LocalDateTime = LocalDateTime.now()
    ): AnkiFlashCard {
        if (quality !in 0..3) {
            throw IllegalArgumentException("Quality must be between 0 and 3")
        }

        return when (card.state) {
            CardState.NEW, CardState.LEARNING, CardState.RELEARNING -> handleLearningOrRelearning(
                card,
                quality,
                currentTime
            )

            CardState.REVIEW -> handleReview(card, quality, currentTime)
        }
    }

    // Xử lý thẻ trong giai đoạn học hoặc học lại
    private fun handleLearningOrRelearning(card: AnkiFlashCard, quality: Int, currentTime: LocalDateTime)
    : AnkiFlashCard {
        val isRelearning = card.state == CardState.RELEARNING
        val newCard = card.copy()

        when (quality) {
            0 -> { // Again: Quay lại bước đầu
                newCard.learningStep = 0
                var time = currentTime.plusMinutes(learningSteps[0].toLong())
                newCard.nextReviewDate = time.toString()
                if (isRelearning) {
                    newCard.easeFactor = max(1.3, newCard.easeFactor - 0.2) // Giảm độ dễ
                }
            }

            1 -> { // Hard: Ôn lại sau bước hiện tại hoặc bước trước
                newCard.learningStep = max(0, newCard.learningStep - 1)
                newCard.nextReviewDate = currentTime.plusMinutes(learningSteps[newCard.learningStep].toLong()).toString()
                if (isRelearning) {
                    newCard.easeFactor = max(1.3, newCard.easeFactor - 0.15)
                }
            }

            2 -> { // Good: Chuyển sang bước tiếp theo hoặc thành Review
                newCard.learningStep += 1
                if (newCard.learningStep >= learningSteps.size) {
                    newCard.state = CardState.REVIEW
                    newCard.interval = graduatingInterval
                    newCard.nextReviewDate = currentTime.plusDays(graduatingInterval.toLong()).toString()
                    newCard.repetitions += 1
                } else {
                    newCard.nextReviewDate =
                        currentTime.plusMinutes(learningSteps[newCard.learningStep].toLong()).toString()
                }
            }

            3 -> { // Easy: Chuyển thẳng sang Review
                newCard.state = CardState.REVIEW
                newCard.interval = (graduatingInterval * easyBonus).toInt()
                newCard.nextReviewDate = currentTime.plusDays(newCard.interval.toLong()).toString()
                newCard.repetitions += 1
                newCard.easeFactor = min(2.5, newCard.easeFactor + 0.15)
            }
        }

        return newCard
    }


    // Xử lý thẻ trong giai đoạn ôn tập
    private fun handleReview(
        card: AnkiFlashCard,
        quality: Int,
        currentTime: LocalDateTime
    ): AnkiFlashCard {
        val newCard = card.copy()

        when (quality) {
            0 -> { // Again: Chuyển sang Relearning
                newCard.state = CardState.RELEARNING
                newCard.learningStep = 0
                newCard.interval =
                    max(minimumInterval, (newCard.interval * relearnMultiplier).toInt())
                newCard.nextReviewDate = currentTime.plusMinutes(learningSteps[0].toLong()).toString()
                newCard.easeFactor = max(1.3, newCard.easeFactor - 0.2)
            }

            1 -> { // Hard: Ôn lại sớm hơn
                newCard.interval = max(minimumInterval, (newCard.interval * hardInterval).toInt())
                newCard.nextReviewDate = currentTime.plusDays(newCard.interval.toLong()).toString()
                newCard.easeFactor = max(1.3, newCard.easeFactor - 0.15)
            }

            2 -> { // Good: Tăng interval bình thường
                newCard.interval = max(
                    minimumInterval,
                    (newCard.interval * newCard.easeFactor * intervalModifier).toInt()
                )
                newCard.nextReviewDate = currentTime.plusDays(newCard.interval.toLong()).toString()
                newCard.repetitions += 1
            }

            3 -> { // Easy: Tăng interval nhiều hơn
                newCard.interval = max(
                    minimumInterval,
                    (newCard.interval * newCard.easeFactor * intervalModifier * easyBonus).toInt()
                )
                newCard.nextReviewDate = currentTime.plusDays(newCard.interval.toLong()).toString()
                newCard.repetitions += 1
                newCard.easeFactor = min(2.5, newCard.easeFactor + 0.15)
            }
        }

        return newCard
    }

    fun handleReviewVersion2(
        card: AnkiFlashCard
    ): MutableList<String> {
        val newCard = card.copy()
        var list = mutableListOf<String>()
        // Again: Chuyển sang Relearning
                list.add("< ${learningSteps[1]} ph")
                // Hard: Ôn lại sớm hơn
                var interval = max(minimumInterval, (newCard.interval * hardInterval).toInt())
                list.add("< ${interval} ng")
             // Good: Tăng interval bình thường
                 interval = max(
                    minimumInterval,
                    (newCard.interval * newCard.easeFactor * intervalModifier).toInt()
                )
                list.add("< ${interval} ng")
             // Easy: Tăng interval nhiều hơn
                 interval = max(
                    minimumInterval,
                    (newCard.interval * newCard.easeFactor * intervalModifier * easyBonus).toInt()
                )
                list.add("< ${interval} ng")
        return list
    }
}