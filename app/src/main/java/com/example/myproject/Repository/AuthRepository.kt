package com.example.myproject.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth

class AuthRepository {
    private val mAuth = FirebaseAuth.getInstance()

    fun login(strEmail: String, strMk: String): LiveData<String> {
        val result = MutableLiveData<String>()
        Log.d("testttt", "Đã gọi hàm repository")

        mAuth.signInWithEmailAndPassword(strEmail, strMk)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    result.postValue("Đăng nhập thành công: ${user?.email}")
                } else {
                    val exception = task.exception
                    if (exception is FirebaseNetworkException) {
                        result.postValue("Lỗi mạng: Vui lòng kiểm tra kết nối")
                    } else {
                        result.postValue("Đăng nhập thất bại: ${exception?.message}")
                        Log.e("DEBUGgg", "${exception?.message}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                result.postValue("Lỗi đăng nhập: ${exception.message}")
                Log.e("DEBUGgg", "Thất bại hoàn toàn: ${exception.message}")
            }

        return result
    }
    fun register(email:String, mk:String): LiveData<String>{
        val result = MutableLiveData<String>()
        mAuth.createUserWithEmailAndPassword(email,mk).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                val user = mAuth.currentUser
                user?.sendEmailVerification()?.addOnCompleteListener{
                    veritask ->
                    if (veritask.isSuccessful) {
                        result.postValue( "Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản.")
                    } else {
                        result.postValue("Lỗi khi gửi email xác nhận")
                    }
                }
            }else {
                val exception = task.exception
                result.postValue("Đăng nhập thất bại: ${exception?.message}")
                Log.e("DEBUGgg", "${exception?.message}")
            }

    }
    .addOnFailureListener { exception ->
        result.postValue("Lỗi đăng nhập: ${exception.message}")
        Log.e("DEBUGgg", "Thất bại hoàn toàn: ${exception.message}")
    }
        return result
        }
    }
