package com.example.myproject.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myproject.Model.User
import com.example.myproject.R
import com.example.myproject.ViewModel.HomeViewmodel
import com.example.myproject.databinding.ActivityMainBinding

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
                R.id.nav_more -> {
                    try {
                        val user = homeviewModel.getUser {
                            if (it != null) {
                            val userForProfile: User = it
                            val intent = Intent(this, Profile::class.java)
                            intent.putExtra("user121", userForProfile)
                            startActivity(intent)
                        } else {
                            Log.e("avatarClick", "User is null")
                        }
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("avatarClick", "Error: ${e.message}")
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
}