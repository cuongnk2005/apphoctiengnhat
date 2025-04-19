package com.example.myproject.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth

class AuthRepository {
    private val mAuth = FirebaseAuth.getInstance()
    fun login(strEmail: String, strMk: String): LiveData<String> {
        var result = MutableLiveData<String>()

        mAuth.signInWithEmailAndPassword(strEmail, strMk).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = mAuth.currentUser
                result.value = "Đăng nhập thành công: ${user?.email}"
            } else {
                val exception = task.exception
                if (exception is FirebaseNetworkException) {
                    result.value = "Lỗi mạng: Vui lòng kiểm tra kết nối"
                } else {
                    result.value = "Đăng nhập thất bại: ${exception?.message}"
                    Log.e("DEBUGgg", "${exception?.message}")
                }

            }
        }
        return result
    }
}