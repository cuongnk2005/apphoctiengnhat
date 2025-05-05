package com.example.myproject.Repository
import android.content.Context
import android.util.Base64
import com.imagekit.android.ImageKit
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myproject.Model.User
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class AuthRepository {
    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private var usersRef = db.getReference()
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
                        user.uid.let{
                           var nUser = User(email,mk, username = "", url = "")
                             pushUser(nUser, user.uid);
                        }
                    } else {
                        result.postValue("Lỗi khi gửi email xác nhận")
                    }
                }
            }else {
                val exception = task.exception
                result.postValue("Đăng ký thất bại: ${exception?.message}")
                Log.e("DEBUGgg", "${exception?.message}")
            }

    }
    .addOnFailureListener { exception ->
        result.postValue("Lỗi đăng nhập: ${exception.message}")
        Log.e("DEBUGgg", "Thất bại hoàn toàn: ${exception.message}")
    }
        return result
        }
    private fun pushUser(user:User, uid:String){

            usersRef.child("users").child(uid).setValue(user)
            .addOnSuccessListener {
                Log.d("sucessAddUser", "them thanh cong")
            }
            .addOnFailureListener{ error ->
                Log.e("errorForPushUser", "loi ${error}")
            }
    }

}





