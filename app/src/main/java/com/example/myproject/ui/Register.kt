package com.example.myproject.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myproject.R
import com.example.myproject.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        events()
    }

    private fun events() {
        binding.btnSignUp.setOnClickListener {

        }
    }

    private fun checkValid(username: String, password:String, confirmPassword: String) {
        when {
            username.isEmpty() -> {
                binding.txtUser.error = "Please enter username!"
                binding.txtUser.requestFocus()
            }
            password.isEmpty() -> {
                binding.txtPassword.error = "Please enter password!"
                binding.txtPassword.requestFocus()
            }
            confirmPassword.isEmpty() -> {
                binding.txtConfirmPassword.error = "Please enter confirm password!"
            }
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