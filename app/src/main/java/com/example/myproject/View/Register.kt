package com.example.myproject.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
            var password= binding.txtPassword.text.toString()
            var confirmPassword = binding.txtConfirmPassword.text.toString()
            checkValid(gmail,password,confirmPassword)
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

    private fun checkValid(username: String, password:String, confirmPassword: String) {
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
            registerViewModel.register(username,password).observe(this){ message ->
                showSusscessDialog(this,message)
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
    private fun showSusscessDialog(context: Context, errorMessage: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Thông Báo")
        builder.setMessage(errorMessage)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            binding.loading.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.Main) {
                delay(2000)
                binding.loading.visibility = View.GONE
                val intent = Intent(this@Register, UI_Login::class.java)
                startActivity(intent)
                finish()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}