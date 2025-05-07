package com.example.myproject.Repository


import android.content.Context
import com.example.myproject.Model.DictionaryEntry
import com.example.myproject.Model.Example

class DictionaryRepository(private val context: Context) {

    // Giả lập dữ liệu từ điển (trong thực tế bạn sẽ lấy từ database)
    private val mockDictionary = listOf(
        DictionaryEntry(
            1,
            "りんご",
            "ringo",
            "Quả táo",
            listOf(Example("私は毎日りんごを食べます。", "Tôi ăn một quả táo mỗi ngày.")),
            "Danh từ"
        ),
        DictionaryEntry(
            2,
            "学生",
            "gakusei",
            "Học sinh, sinh viên",
            listOf(Example("彼は大学の学生です。", "Anh ấy là sinh viên đại học.")),
            "Danh từ"
        ),
        DictionaryEntry(
            3,
            "食べる",
            "taberu",
            "Ăn",
            listOf(Example("朝ごはんを食べましたか？", "Bạn đã ăn sáng chưa?")),
            "Động từ"
        ),
        DictionaryEntry(
            4,
            "水",
            "mizu",
            "Nước",
            listOf(Example("水を飲みたいです。", "Tôi muốn uống nước.")),
            "Danh từ"
        ),
        DictionaryEntry(
            5,
            "行く",
            "iku",
            "Đi",
            listOf(Example("学校に行きます。", "Tôi đi đến trường.")),
            "Động từ"
        )
    )

    // Tìm kiếm từ
    fun searchWord(query: String, isJapaneseToVietnamese: Boolean): DictionaryEntry? {
        return if (isJapaneseToVietnamese) {
            // Tìm từ tiếng Nhật
            mockDictionary.find {
                it.word.contains(query) || it.reading.contains(query)
            }
        } else {
            // Tìm từ tiếng Việt
            mockDictionary.find {
                it.meaning.contains(query, ignoreCase = true)
            }
        }
    }

    // Tìm kiếm nhiều kết quả
    fun searchWords(query: String, isJapaneseToVietnamese: Boolean): List<DictionaryEntry> {
        val searchQuery = query.lowercase()

        return if (isJapaneseToVietnamese) {
            // Tìm từ tiếng Nhật
            mockDictionary.filter {
                it.word.lowercase().contains(searchQuery) ||
                        it.reading.lowercase().contains(searchQuery)
            }
        } else {
            // Tìm từ tiếng Việt
            mockDictionary.filter {
                it.meaning.lowercase().contains(searchQuery)
            }
        }
    }
}