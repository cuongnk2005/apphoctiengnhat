package com.example.myproject.View

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.myproject.Model.ToastType
import com.example.myproject.R
import com.example.myproject.ViewModel.LoginViewModel
import com.example.myproject.databinding.ActivityUiLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UI_Login : AppCompatActivity() {
    private val authModel: LoginViewModel by viewModels()
    lateinit var binding: ActivityUiLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUiLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        events()

    }

    private fun events() {
        binding.btnLogin.setOnClickListener {
            val username = binding.txtUser.text.toString().trim()
            val pass = binding.txtPassword.text.toString().trim()
            checkValid(username, pass)
        }


        binding.btnRegister.setOnClickListener{
            binding.loading.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.Main) {
                delay(500)
                binding.loading.visibility = View.GONE
                val intent = Intent(this@UI_Login, Register::class.java)
                startActivity(intent)
                finish()
            }

        }

    }

    private fun checkValid(username: String, password: String) {
        when {
            username.isEmpty() -> {
                binding.txtUser.error = "Vui lòng nhập email!"
                binding.txtUser.requestFocus()
            }

            password.isEmpty() -> {
                binding.txtPassword.error = "Vui lòng nhập password!"
                binding.txtPassword.requestFocus()
            }
            else -> {
                Log.d("ttttt", "có chạy hàm này")
                attemptLogin(username, password)
            }
        }
    }

    private fun attemptLogin(username: String, password: String) {
        authModel.login(username, password).observe(this) { msg ->
            if (msg == null || msg.isEmpty()) {
//                showErrorDialog(this, "Không nhận được phản hồi.")
                showCustomToast(
                    context = this,
                    title = "Lỗi",
                    message = "Không nhận được phản hồi!",
                    type = ToastType.ERROR
                )
            }else if(msg == "Vui lòng xác thực email!"){
//                showErrorDialog(this,messenger)
                showCustomToast(
                    context = this,
                    title = "Lỗi",
                    message = msg,
                    type = ToastType.ERROR
                )
            }
            else if (msg.contains("lỗi", ignoreCase = true)) {
//                showErrorDialog(this, messenger)
                showCustomToast(
                    context = this,
                    title = "Lỗi",
                    message = msg,
                    type = ToastType.ERROR
                )
            } else {
                showCustomToast(
                    context = this,
                    title = "Thành công",
                    message = "Đăng nhập thành công!",
                    type = ToastType.SUCCESS
                )
                loading(this)
                hideKeyboad()
            }
        }
    }

    private fun showToast(msg:String) {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }

    private fun showCustomToast(context: Context, title: String, message: String, type: ToastType) {
        val layout = LayoutInflater.from(context).inflate(R.layout.custom_toast, null)

        // Thiết lập nội dung
        layout.findViewById<TextView>(R.id.toast_title).text = title
        layout.findViewById<TextView>(R.id.toast_message).text = message

        // Thiết lập icon và màu sắc dựa trên loại thông báo
        val iconView = layout.findViewById<ImageView>(R.id.toast_icon)
        val container = layout.findViewById<LinearLayout>(R.id.custom_toast_container)

        when (type) {
            ToastType.SUCCESS -> {
                iconView.setImageResource(R.drawable.ic_success)
                container.background = ContextCompat.getDrawable(context, R.drawable.toast_success_bg)
            }
            ToastType.ERROR -> {
                iconView.setImageResource(R.drawable.ic_error)
                container.background = ContextCompat.getDrawable(context, R.drawable.toast_error_bg)
            }
            ToastType.WARNING -> {
                iconView.setImageResource(R.drawable.ic_warning)
                container.background = ContextCompat.getDrawable(context, R.drawable.toast_warning_bg)
            }
            ToastType.INFO -> {
                iconView.setImageResource(R.drawable.ic_info)
                container.background = ContextCompat.getDrawable(context, R.drawable.toast_info_bg)
            }
        }

        // Tạo và hiển thị toast
        val toast = Toast(context)
        toast.setGravity(Gravity.TOP, 0, 100)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()

        // Xử lý nút đóng
        layout.findViewById<ImageView>(R.id.close_button).setOnClickListener {
            toast.cancel()
        }
    }

    private fun hideKeyboad() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        val view = currentFocus
        if(view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
//
//    private fun showErrorDialog(context: Context, errorMessage: String) {
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("Lỗi")
//        builder.setMessage(errorMessage)
//        builder.setPositiveButton("OK") { dialog, _ ->
//            dialog.dismiss()
//        }
//        val dialog = builder.create()
//        dialog.show()
//    }
    private fun loading(context: Context) {
            binding.loading.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.Main) {
                delay(1000)
                binding.loading.visibility = View.GONE
                val intent = Intent(this@UI_Login, MainActivity::class.java)
                startActivity(intent)
                finish()
        }
    }

}