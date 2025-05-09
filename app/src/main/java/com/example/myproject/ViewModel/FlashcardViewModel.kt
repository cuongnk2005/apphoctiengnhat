package com.example.myproject.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.Model.FlashcardModel
import com.example.myproject.Model.FlashcardSetInfo
import com.example.myproject.R
import com.example.myproject.Repository.TopicRepository
import kotlinx.coroutines.launch


class FlashcardViewModel : ViewModel() {
    private val topicRepository = TopicRepository()
    // LiveData for current flashcard
    private val _currentFlashcard = MutableLiveData<FlashcardModel>()
    val currentFlashcard: LiveData<FlashcardModel> get() = _currentFlashcard


    // LiveData for progress
    private val _progressText = MutableLiveData<String>()
    val progressText: LiveData<String> get() = _progressText

    private val _progressPercentage = MutableLiveData<Int>()
    val progressPercentage: LiveData<Int> = _progressPercentage

    // List of flashcards for current set
    private var flashcards = mutableListOf<FlashcardModel>()

    // Current position in flashcard list
    private var currentPosition = 0

    // Flashcard set info
    private lateinit var flashcardSet: FlashcardSetInfo

    // Load flashcards from database by set ID
    fun loadFlashcards(setId: String) {
        viewModelScope.launch {
         val topic = topicRepository.getTopicByID(setId)
            if(topic!= null){
                flashcards = topic.vocabulary_list

                flashcardSet = FlashcardSetInfo(
                    id = 1,
                    title = "Từ vựng tiếng Nhật cơ bản",
                    totalCards = flashcards.size,
                    learnedCards = 0,
                    level = "N5",
                    category = "Cơ bản"
                )
                if (flashcards.isNotEmpty()) {
                    _currentFlashcard.value = flashcards[0]
                    updateProgress()
                }
            }
        }


    }


    // For testing/demo purposes
    fun loadSampleFlashcards() {
//        flashcards = getSampleFlashcards()
        flashcardSet = FlashcardSetInfo(
            id = 1,
            title = "Từ vựng tiếng Nhật cơ bản",
            totalCards = flashcards.size,
            learnedCards = 0,
            level = "N5",
            category = "Cơ bản"
        )

        // Set initial flashcard
        if (flashcards.isNotEmpty()) {
            _currentFlashcard.value = flashcards[0]
            updateProgress()
        }
    }

    // Move to next flashcard
    fun nextFlashcard() {
        if (currentPosition < flashcards.size - 1) {
            currentPosition++
            _currentFlashcard.value = flashcards[currentPosition]
            updateProgress()
        } else {
            // End of deck, show completion or loop back
            // For now, we'll loop back to the first card
            currentPosition = 0
            _currentFlashcard.value = flashcards[currentPosition]
            updateProgress()
        }
    }

    // Move to previous flashcard
    fun previousFlashcard() {
        if (currentPosition > 0) {
            currentPosition--
            _currentFlashcard.value = flashcards[currentPosition]
            updateProgress()
        }
    }

    // Mark current flashcard as known
    fun markAsKnown() {
        flashcards[currentPosition].isKnown = true
        updateProgress()
        // In a real app, save this state to persistent storage
    }

    // Mark current flashcard as unknown/needs review
    fun markAsUnknown() {
        flashcards[currentPosition].isKnown = false
        updateProgress()
        // In a real app, save this state to persistent storage
    }

    // Reset progress for all flashcards
    fun resetAllFlashcardsProgress() {
        flashcards.forEach { it.isKnown = false }
        updateProgress()
        // In a real app, save this state to persistent storage
    }

    // Get audio resource ID for current flashcard
//    fun getCurrentFlashcardAudioResourceId(): Int {
        // In a real app, this would return actual audio resources
        // For now, we'll return a placeholder resource
//        return R.raw.sample_audio
//    }
//    }
//    }

    // Get all flashcards in the current set
    fun getAllFlashcards(): List<FlashcardModel> {
        return flashcards
    }

    // Get information about the current flashcard set
    fun getFlashcardSetInfo(): FlashcardSetInfo {
        return flashcardSet
    }

    // Update progress indicators
    private fun updateProgress() {
        val totalCards = flashcards.size
        val currentCard = currentPosition + 1
        _progressText.value = "$currentCard/$totalCards từ"

        val progressPercentage = (currentCard * 100) / totalCards
        _progressPercentage.value = progressPercentage

        // Update flashcard set info
        val learnedCards = flashcards.count { it.isKnown }
        flashcardSet = flashcardSet.copy(learnedCards = learnedCards)
    }

    // Sample data for testing
//    private fun getSampleFlashcards(): MutableList<FlashcardModel> {
//        return mutableListOf(
//            FlashcardModel(
//                id = 1,
//                kanji = "水",
//                hiragana = "みず",
//                romaji = "mizu",
//                meaning = "Nước",
//                definition = "Nước: chất lỏng trong, không màu dùng để uống và sinh hoạt hàng ngày.",
//                exampleJapanese = "水を飲みます。",
//                exampleReading = "みずをのみます。",
//                exampleMeaning = "Tôi uống nước.",
//                isKnown = false
//            ),
//            FlashcardModel(
//                id = 2,
//                kanji = "火",
//                hiragana = "ひ",
//                romaji = "hi",
//                meaning = "Lửa",
//                definition = "Lửa: hiện tượng cháy tạo ra nhiệt và ánh sáng.",
//                exampleJapanese = "火をつけます。",
//                exampleReading = "ひをつけます。",
//                exampleMeaning = "Tôi đốt lửa.",
//                isKnown = false
//            ),
//            FlashcardModel(
//                id = 3,
//                kanji = "木",
//                hiragana = "き",
//                romaji = "ki",
//                meaning = "Cây",
//                definition = "Cây: thực vật có thân gỗ cứng, sinh trưởng nhiều năm.",
//                exampleJapanese = "木を植えます。",
//                exampleReading = "きをうえます。",
//                exampleMeaning = "Tôi trồng cây.",
//                isKnown = false
//            ),
//            FlashcardModel(
//                id = 4,
//                kanji = "金",
//                hiragana = "きん",
//                romaji = "kin",
//                meaning = "Vàng/Kim loại",
//                definition = "Kim loại quý màu vàng, được sử dụng làm trang sức và tiền tệ.",
//                exampleJapanese = "金の指輪です。",
//                exampleReading = "きんのゆびわです。",
//                exampleMeaning = "Đây là nhẫn vàng.",
//                isKnown = false
//            ),
//            FlashcardModel(
//                id = 5,
//                kanji = "土",
//                hiragana = "つち",
//                romaji = "tsuchi",
//                meaning = "Đất",
//                definition = "Đất: vật chất tự nhiên trên bề mặt trái đất, dùng để trồng cây.",
//                exampleJapanese = "土から植物が育ちます。",
//                exampleReading = "つちからしょくぶつがそだちます。",
//                exampleMeaning = "Cây cối mọc lên từ đất.",
//                isKnown = false
//            ),
//            FlashcardModel(
//                id = 6,
//                kanji = "日",
//                hiragana = "ひ",
//                romaji = "hi",
//                meaning = "Ngày/Mặt trời",
//                definition = "Ngày: đơn vị thời gian 24 giờ. Mặt trời: thiên thể phát sáng trung tâm của hệ mặt trời.",
//                exampleJapanese = "今日は晴れの日です。",
//                exampleReading = "きょうははれのひです。",
//                exampleMeaning = "Hôm nay là ngày nắng.",
//                isKnown = false
//            ),
//            FlashcardModel(
//                id = 7,
//                kanji = "月",
//                hiragana = "つき",
//                romaji = "tsuki",
//                meaning = "Mặt trăng/Tháng",
//                definition = "Vệ tinh tự nhiên duy nhất của Trái Đất, hoặc đơn vị thời gian gồm khoảng 30 ngày.",
//                exampleJapanese = "夜の月がきれいです。",
//                exampleReading = "よるのつきがきれいです。",
//                exampleMeaning = "Mặt trăng đêm rất đẹp.",
//                isKnown = false
//            ),
//            FlashcardModel(
//                id = 8,
//                kanji = "山",
//                hiragana = "やま",
//                romaji = "yama",
//                meaning = "Núi",
//                definition = "Núi: địa hình nhô cao trên bề mặt trái đất, lớn hơn đồi.",
//                exampleJapanese = "富士山は日本で一番高い山です。",
//                exampleReading = "ふじさんはにほんでいちばんたかいやまです。",
//                exampleMeaning = "Núi Phú Sĩ là ngọn núi cao nhất Nhật Bản.",
//                isKnown = false
//            ),
//            FlashcardModel(
//                id = 9,
//                kanji = "川",
//                hiragana = "かわ",
//                romaji = "kawa",
//                meaning = "Sông",
//                definition = "Sông: dòng nước tự nhiên chảy từ cao xuống thấp và đổ ra biển hoặc hồ.",
//                exampleJapanese = "川で泳ぎます。",
//                exampleReading = "かわでおよぎます。",
//                exampleMeaning = "Tôi bơi ở sông.",
//                isKnown = false
//            ),
//            FlashcardModel(
//                id = 10,
//                kanji = "人",
//                hiragana = "ひと",
//                romaji = "hito",
//                meaning = "Người",
//                definition = "Người: loài sinh vật có khả năng tư duy và sáng tạo.",
//                exampleJapanese = "あの人は先生です。",
//                exampleReading = "あのひとはせんせいです。",
//                exampleMeaning = "Người kia là giáo viên.",
//                isKnown = false
//            )
//        )
//    }
}