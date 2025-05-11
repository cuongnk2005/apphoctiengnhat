package com.example.myproject.Repository
import android.content.Context
import android.util.Base64
import com.imagekit.android.ImageKit
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myproject.Model.OldTopic
import com.example.myproject.Model.Topic
import com.example.myproject.Model.User
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.Callback
import okhttp3.EventListener


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
                    if(user!= null && user.isEmailVerified){
                        result.postValue("Đăng nhập thành công: ${user?.email}")
                    } else{
                        result.postValue("Vui lòng xác thực email")
                    }

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
                result.postValue("Lỗi đăng nhập. Vui lòng kiểm tra lại email hoặc password!")
                Log.e("DEBUGgg", "Thất bại hoàn toàn: ${exception.message}")
            }

        return result
    }
    fun register(email:String, mk:String): LiveData<String>{
        val result = MutableLiveData<String>()
        mAuth.createUserWithEmailAndPassword(email,mk).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                val user = mAuth.currentUser
                user?.sendEmailVerification()?.addOnCompleteListener{ veritask ->
                    if (veritask.isSuccessful) {
                        result.postValue( "Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản.")
                        user.uid.let{
                           var nUser = User(email,mk, username = "", url = "", listTopicStuded = ArrayList())
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
        result.postValue("Lỗi đăng ký. Vui lòng kiểm tra lại mạng!")
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

     fun getUserByID(callback: (User?) -> Unit){
         Log.d("fsfss","co chay get userripository")
        val userID = mAuth.currentUser?.uid
        val userRef = usersRef.child("users").child(userID.toString())
        userRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                Log.d("userr","${user.toString()}")
                callback(user)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("errorForGetUser", "loi ${error}")
            }
        }
        )



}
    fun updateUserByID( map: Map<String, Any>){
        val userID = mAuth.currentUser?.uid.toString()
           val newUserRef = usersRef.child("users").child(userID)
           newUserRef.updateChildren(map)
               .addOnSuccessListener {
                   Log.d("sucessAddUser", "them thanh cong")
               }
               .addOnFailureListener{ error ->
                   Log.e("errorForPushUser", "loi ${error}")
               }
    }

    suspend fun getOldTopicsFromUser():MutableList<String> {
        return withContext(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val userID = mAuth.currentUser?.uid.toString()
                val userRef = usersRef.child("users").child(userID).child("listTopicStuded")
                val snapshot = userRef.get().await()
                var listOldTopic = ArrayList<String>()
                for (item in snapshot.children) {
                    var oldtopic = item.getValue(OldTopic::class.java)
                    if (oldtopic != null) {
                        listOldTopic.add(oldtopic.id)
                    }
                }

                listOldTopic
            } catch (e:Exception ){
                mutableListOf<String>()
            }

        }
    }

}





