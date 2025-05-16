package com.example.myproject.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.example.myproject.Model.ToastType
import com.example.myproject.R
import com.example.myproject.ViewModel.RegisterViewModel
import com.example.myproject.databinding.ActivityRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Register : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        events()
    }

    private fun events() {
        binding.btnSignUp.setOnClickListener {
        var gmail = binding.txtUser.text.toString()
            var name = binding.txtName.text.toString()
            var password= binding.txtPassword.text.toString()
            var confirmPassword = binding.txtConfirmPassword.text.toString()
            checkValid(gmail,name,password,confirmPassword)
        }
        binding.btnLogin.setOnClickListener{
            binding.loading.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.Main) {
                delay(500)
                binding.loading.visibility = View.GONE
                val intent = Intent(this@Register, UI_Login::class.java)
                startActivity(intent)
            }
        }
    }

    private fun checkValid(email:String,username: String, password:String, confirmPassword: String) {
        when {
            username.isEmpty() -> {
                binding.txtUser.error = "Please enter username!"
                binding.txtUser.requestFocus()
                return
            }
            password.isEmpty() -> {
                binding.txtPassword.error = "Please enter password!"
                binding.txtPassword.requestFocus()
                return
            }
            confirmPassword.isEmpty() -> {
                binding.txtConfirmPassword.error = "Please enter confirm password!"
                return
            }
        }
        if(password!= confirmPassword){
            binding.txtConfirmPassword.error = "Please enter the same password!"
        } else {
            registerViewModel.register(email,username,password).observe(this){ msg ->
//                showSusscessDialog(this,message)
                showCustomToast(
                    context = this,
                    title = "Thành công",
                    message = msg,
                    type = ToastType.SUCCESS
                )
                loading(this)
                hideKeyboad()
            }
        }
    }


    private fun hideKeyboad() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        val view = currentFocus
        if(view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

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
//    private fun showSusscessDialog(context: Context, errorMessage: String) {
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("Thông Báo")
//        builder.setMessage(errorMessage)
//        builder.setPositiveButton("OK") { dialog, _ ->
//            dialog.dismiss()
//            binding.loading.visibility = View.VISIBLE
//            GlobalScope.launch(Dispatchers.Main) {
//                delay(2000)
//                binding.loading.visibility = View.GONE
//                val intent = Intent(this@Register, UI_Login::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }
//        val dialog = builder.create()
//        dialog.show()
//    }

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

    private fun loading(context: Context) {
        binding.loading.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            binding.loading.visibility = View.GONE
            val intent = Intent(this@Register, UI_Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}