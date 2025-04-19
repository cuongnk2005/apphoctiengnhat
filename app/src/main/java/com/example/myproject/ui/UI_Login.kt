package com.example.myproject.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myproject.R
import com.example.myproject.databinding.ActivityUiLoginBinding

class UI_Login : AppCompatActivity() {
    lateinit var binding: ActivityUiLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUiLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        events()

    }

    private fun events() {

        binding.imgFb.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"))
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.txtUser.text.toString().trim()
            val pass = binding.txtPassword.text.toString().trim()

            checkValid(username, pass)
            attemptLogin(username,pass)
            hideKeyboad()

        }

        binding.txtForgotPass.setOnClickListener {

        }

        binding.signUp.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

    }

    private fun checkValid(username: String, password: String) {
        when {
            username.isEmpty() -> {
                binding.txtUser.error = "Please enter username!"
                binding.txtUser.requestFocus()
            }

            password.isEmpty() -> {
                binding.txtPassword.error = "Please enter password!"
                binding.txtPassword.requestFocus()
            }
            else -> {
                attemptLogin(username, password)
            }
        }
    }

    private fun attemptLogin(username: String, password: String) {
        val user = "kiet"
        val pass = "123"
        if (username==user && password == pass) {
            Intent(this, Home::class.java).also {
                startActivity(it)
            }
            showToast("Đăng nhập thành công")
            hideKeyboad()
        } else {
            showErrorDialog(this, "Thông tin username hoặc mật khẩu không chính xác. Vui lòng nhập lại!")
            binding.txtUser.requestFocus()
        }
    }



    private fun showToast(msg:String) {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboad() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        val view = currentFocus
        if(view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showErrorDialog(context: Context, errorMessage: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Lỗi")
        builder.setMessage(errorMessage)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

}