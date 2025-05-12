package com.example.myproject.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myproject.Model.User
import com.example.myproject.R
import com.example.myproject.ViewModel.HomeViewmodel
import com.example.myproject.databinding.ActivityMainBinding
import com.google.android.play.core.integrity.bu

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val homeviewModel: HomeViewmodel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mặc định là Trang chủ
        loadFragment(HomeFragment())
        binding.bottomNav.selectedItemId = R.id.nav_home
        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_search -> loadFragment(DictionarySearchFragment())
                R.id.nav_study -> loadFragment(LearnVocabularyFragment())
                R.id.nav_addFlashCard -> loadActivity(AddFlashCard::class.java, null, true, true)
                R.id.nav_profile -> {
                    val user = homeviewModel.getUserInViewModel()
                    Log.d("DEBUG", "User data from ViewModel: $user")

                    if (user != null) {
                        val bundle = Bundle().apply {
                            putSerializable("user121", user)
                        }
                        loadActivity(Profile::class.java, bundle, true, true)
                    } else {
                        Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            true
        }
    }

     fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
    fun loadLearFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
        binding.bottomNav.selectedItemId = R.id.nav_study
    }
    fun loadActivity(
        targetActivity: Class<*>,
        extras: Bundle? = null,
        clearTop: Boolean = false,
        animate: Boolean = true
    ) {
        val intent = Intent(this, targetActivity).apply {
            // Xóa các activity trước đó nếu cần
            if (clearTop) {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }

            // Thêm dữ liệu nếu có
            extras?.let {
                putExtras(it)
            }
        }

        startActivity(intent)

        // Áp dụng animation nếu được yêu cầu
        if (animate) {
            overridePendingTransition(
                R.animator.slide_in_right,  // Animation slide vào từ phải
                R.animator.slide_out_left   // Animation slide ra sang trái
            )
        }
    }
}