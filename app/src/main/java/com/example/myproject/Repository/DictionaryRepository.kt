package com.example.myproject.Repository


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.myproject.Api.RetrofitClient
import com.example.myproject.Model.DictionaryEntry
import com.example.myproject.Model.toDictionaryEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class DictionaryRepository(private val context: Context) {
    private val apiService = RetrofitClient.jishoApiService
    private val TAG = "DictionaryRepository"

    // Kiểm tra kết nối internet
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    // Tìm kiếm một từ
    suspend fun searchWord(query: String): DictionaryEntry? {
        return if (isNetworkAvailable()) {
            try {
                searchWordOnline(query)
            } catch (e: Exception) {
                Log.e(TAG, "Error searching online: ${e.message}", e)
                searchWordOffline(query)
            }
        } else {
            Log.d(TAG, "No network connection, using offline data")
            searchWordOffline(query)
        }
    }

    // Tìm kiếm nhiều từ
    suspend fun searchWords(query: String): List<DictionaryEntry> {
        return if (isNetworkAvailable()) {
            try {
                searchWordsOnline(query)
            } catch (e: Exception) {
                Log.e(TAG, "Error searching online: ${e.message}", e)
                searchWordsOffline(query)
            }
        } else {
            Log.d(TAG, "No network connection, using offline data")
            searchWordsOffline(query)
        }
    }

    // Tìm kiếm online qua API
    private suspend fun searchWordOnline(query: String): DictionaryEntry? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.searchWords(query)
                if (response.data.isNotEmpty()) {
                    response.data.first().toDictionaryEntry(1)
                } else {
                    null
                }
            } catch (e: IOException) {
                Log.e(TAG, "Network error: ${e.message}", e)
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "API error: ${e.message}", e)
                throw e
            }
        }
    }

    // Tìm kiếm nhiều từ online
    private suspend fun searchWordsOnline(query: String): List<DictionaryEntry> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.searchWords(query)
                response.data.mapIndexed { index, jishoEntry ->
                    jishoEntry.toDictionaryEntry(index + 1)
                }
            } catch (e: IOException) {
                Log.e(TAG, "Network error: ${e.message}", e)
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "API error: ${e.message}", e)
                throw e
            }
        }
    }

    // Dữ liệu offline cho fallback
    private val mockDictionary = listOf(
        DictionaryEntry(
            1,
            "りんご",
            "ringo",
            "Quả táo",
            "Quả của cây táo, có hình tròn với vỏ màu đỏ, vàng hoặc xanh lá cây",
            "私は毎日りんごを食べます。",
            "Tôi ăn một quả táo mỗi ngày.",
            "Danh từ"
        ),
        DictionaryEntry(
            2,
            "学生",
            "gakusei",
            "Học sinh, sinh viên",
            "Là người đang theo học tại các cơ sở giáo dục.",
            "彼は大学の学生です。",
            "Anh ấy là sinh viên đại học.",
            "Danh từ"
        ),
        DictionaryEntry(
            3,
            "食べる",
            "taberu",
            "Ăn",
            "Là hành động đưa thức ăn vào cơ thể để nuôi sống và duy trì sức khỏe.",
            "朝ごはんを食べましたか？",
            "Bạn đã ăn sáng chưa?",
            "Động từ"
        ),
        DictionaryEntry(
            4,
            "水",
            "mizu",
            "Nước",
            "Là chất lỏng không màu, không mùi, cần thiết cho sự sống.",
            "水を飲みたいです。",
            "Tôi muốn uống nước.",
            "Danh từ"
        ),
        DictionaryEntry(
            5,
            "行く",
            "iku",
            "Đi",
            "Là hành động di chuyển từ nơi này đến nơi khác.",
            "学校に行きます。",
            "Tôi đi đến trường.",
            "Động từ"
        ),
        DictionaryEntry(
            6,
            "犬",
            "inu",
            "Con chó",
            "Loài động vật có vú thuộc họ Canidae, thường được nuôi làm thú cưng hoặc để canh gác.",
            "私は犬が好きです。",
            "Tôi thích chó.",
            "Danh từ"
        )
    )

    // Tìm kiếm offline
    fun searchWordOffline(query: String): DictionaryEntry? {
        val searchQuery = query.lowercase()
        return mockDictionary.find {
            it.word.lowercase().contains(searchQuery) ||
                    it.reading.lowercase().contains(searchQuery) ||
                    it.meaning.lowercase().contains(searchQuery)
        }
    }

    // Tìm kiếm nhiều từ offline
    fun searchWordsOffline(query: String): List<DictionaryEntry> {
        val searchQuery = query.lowercase()
        return mockDictionary.filter {
            it.word.lowercase().contains(searchQuery) ||
                    it.reading.lowercase().contains(searchQuery) ||
                    it.meaning.lowercase().contains(searchQuery)
        }
    }
}