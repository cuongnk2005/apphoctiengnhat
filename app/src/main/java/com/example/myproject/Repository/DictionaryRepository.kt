package com.example.myproject.Repository


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.myproject.Api.Retrofit
import com.example.myproject.Model.DictionaryEntry
import com.example.myproject.Model.toDictionaryEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class DictionaryRepository(private val context: Context) {
    private val api = Retrofit.apiService

    // Kiểm tra kết nối internet
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    // Tìm kiếm
    suspend fun searchWord(query: String): DictionaryEntry? {
        return if (isNetworkAvailable()) {
            try {
                searchWordOnline(query)
            } catch (e: Exception) {
                Log.e("repository dictionary", "loi mang: ${e.message}", e)
                searchWordOffline(query)
            }
        } else {
            Log.d("repository dictionary", "kh co internet")
            searchWordOffline(query)
        }
    }

    // tìm online
    private suspend fun searchWordOnline(query: String): DictionaryEntry? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchWords(query)
                if (response.data.isNotEmpty()) {
                    response.data.first().toDictionaryEntry(1)
                } else {
                    null
                }
            } catch (e: IOException) {
                Log.e("repository dictionary", "loi mang: ${e.message}", e)
                throw e
            } catch (e: Exception) {
                Log.e("repository dictionary", "loi api: ${e.message}", e)
                throw e
            }
        }
    }

    private suspend fun searchWordsOnline(query: String): List<DictionaryEntry> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchWords(query)
                response.data.mapIndexed { index, jishoEntry ->
                    jishoEntry.toDictionaryEntry(index + 1)
                }
            } catch (e: IOException) {
                Log.e("repository dictionary", "loi mang: ${e.message}", e)
                throw e
            } catch (e: Exception) {
                Log.e("repository dictionary", "loi api: ${e.message}", e)
                throw e
            }
        }
    }

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

    fun searchWordOffline(query: String): DictionaryEntry? {
        val searchQuery = query.lowercase()
        return mockDictionary.find {
            it.word.lowercase().contains(searchQuery) ||
                    it.reading.lowercase().contains(searchQuery) ||
                    it.meaning.lowercase().contains(searchQuery)
        }
    }

   
}