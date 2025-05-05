package com.example.myproject.View

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.myproject.Model.CloudinaryHelper
import com.example.myproject.Model.ImageUploader
import com.example.myproject.R
import com.example.myproject.Repository.AuthRepository
import com.example.myproject.databinding.ActivityProfileBinding
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileOutputStream
import com.imagekit.android.ImageKit

class Profile : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    private lateinit var uriImage: Uri
    private val authRepository = AuthRepository()
    private val imageUploader = ImageUploader()
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            binding.profileImage.setImageURI(uri)
            uriImage = uri

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CloudinaryHelper(applicationContext)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        events()
    }

    private fun events() {
        // back lại trang home
        binding.backButton.setOnClickListener{
            AlertDialog.Builder(this)
                .setTitle("Xác nhận lưu")
                .setPositiveButton("Xác nhận") { dialog, _ ->
                    uploadToCloudinary(uriImage)
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
                }
                .setNegativeButton("Hủy", null)
                .show()

        }

        // Xử lý sự kiện thay đổi ảnh đại diện
        binding.profileImage.setOnClickListener {
            imagePickerLauncher.launch("image/*") // Mở thư viện chọn ảnh
        }

        // Xử lý sự kiện thay đổi ảnh qua TextView (nếu người dùng muốn nhấn vào chữ "THAY ẢNH ĐẠI DIỆN")
        binding.changeProfileImage.setOnClickListener {
            imagePickerLauncher.launch("image/*") // Mở thư viện chọn ảnh
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_changeName -> {
                showChangeNameDialog()
                true
            }
            R.id.action_changePassword -> {
                showChangePasswordDialog()
                true
            }
            R.id.action_deleteAccount -> {
                showDeleteAccountDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Hiển thị dialog cho đổi tên
    private fun showChangeNameDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_name, null)
        val nameEditText = dialogView.findViewById<TextInputEditText>(R.id.etNewName)

        AlertDialog.Builder(this)
            .setTitle("Đổi tên người dùng")
            .setView(dialogView)
            .setPositiveButton("Xác nhận") { dialog, _ ->
                val newName = nameEditText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    // Gọi hàm thay đổi tên
                    changeName(newName)
                } else {
                    Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    // Hàm xử lý đổi tên
    private fun changeName(name: String) {
        // Ở đây bạn sẽ thực hiện gọi API hoặc cập nhật dữ liệu vào SharedPreferences hoặc bất kỳ cơ sở dữ liệu nào
        // Ví dụ:
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("user_name", name).apply()

        // Cập nhật UI nếu cần
        // binding.tvUserName.text = name

        Toast.makeText(this, "Đã đổi tên thành công", Toast.LENGTH_SHORT).show()
    }

    // lay file tu uri
//    fun createTempFileFromUri(uri:Uri, context: Context): File?{
//        val inputStream = context.contentResolver.openInputStream(uri)?: return null
//        val filename = "tem_file_${System.currentTimeMillis()}"
//        val temfile = File.createTempFile(filename, ".png", context.cacheDir)
//        temfile.outputStream().use { output ->
//                inputStream.copyTo(output)
//        }
//        return temfile
//    }

    // hàm upload anh bang cloudinary
    private fun uploadToCloudinary(uri: Uri){
        val uploadPreset = "Lear_nihongo"
        MediaManager.get().upload(uri)
            .unsigned(uploadPreset)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    Log.d("Cloudinary","Upload bat dau")
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {

                }

                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    resultData?.let { data ->
                        // Lấy giá trị "url" và cast sang String
                        val url = data["url"] as? String
                        Toast.makeText(this@Profile, "Upload thành công: $url", Toast.LENGTH_LONG)
                            .show()
                        Log.d("sucesss", "upload Thanh cong")
                    }
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Toast.makeText(this@Profile, "Lỗi: ${error.toString()}", Toast.LENGTH_LONG).show()
                    Log.e("loicloud", "Lỗi: ${error.toString()}")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {

                }
            })
            .dispatch()
    }

    // Hiển thị dialog cho đổi mật khẩu
    private fun showChangePasswordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_password, null)
        val currentPasswordEditText = dialogView.findViewById<TextInputEditText>(R.id.etCurrentPassword)
        val newPasswordEditText = dialogView.findViewById<TextInputEditText>(R.id.etNewPassword)
        val confirmPasswordEditText = dialogView.findViewById<TextInputEditText>(R.id.etConfirmPassword)

        AlertDialog.Builder(this)
            .setTitle("Đổi mật khẩu")
            .setView(dialogView)
            .setPositiveButton("Lưu") { dialog, _ ->
                val currentPassword = currentPasswordEditText.text.toString()
                val newPassword = newPasswordEditText.text.toString()
                val confirmPassword = confirmPasswordEditText.text.toString()

                when {
                    currentPassword.isEmpty() -> {
                        Toast.makeText(this, "Vui lòng nhập mật khẩu hiện tại", Toast.LENGTH_SHORT).show()
                    }
                    newPassword.isEmpty() -> {
                        Toast.makeText(this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show()
                    }
                    newPassword != confirmPassword -> {
                        Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Gọi hàm thay đổi mật khẩu
                        changePassword(currentPassword, newPassword)
                    }
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    // Hàm xử lý đổi mật khẩu
    private fun changePassword(currentPassword: String, newPassword: String) {
        // Kiểm tra mật khẩu hiện tại có đúng không
        // Nếu đúng thì cập nhật mật khẩu mới

        // Đây là giả lập việc kiểm tra mật khẩu, trong thực tế bạn cần kiểm tra với API hoặc dữ liệu lưu trữ
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val savedPassword = sharedPreferences.getString("password", "")

        if (currentPassword == savedPassword) {
            // Cập nhật mật khẩu mới
            sharedPreferences.edit().putString("password", newPassword).apply()
            Toast.makeText(this, "Đã đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show()
        }
    }

    // Hiển thị dialog xác nhận xóa tài khoản
    private fun showDeleteAccountDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_account, null)
        val passwordEditText = dialogView.findViewById<TextInputEditText>(R.id.etPasswordConfirm)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Xóa tài khoản") { dialog, _ ->
                val password = passwordEditText.text.toString()
                if (password.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập mật khẩu để xác nhận", Toast.LENGTH_SHORT).show()
                } else {
                    // Gọi hàm xóa tài khoản
                    deleteAccount(password)
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    // Hàm xử lý xóa tài khoản
    private fun deleteAccount(password: String) {
        // Kiểm tra mật khẩu có đúng không
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val savedPassword = sharedPreferences.getString("password", "")

        if (password == savedPassword) {
            // Thực hiện xóa tài khoản
            // Xóa dữ liệu người dùng khỏi SharedPreferences hoặc gọi API xóa tài khoản
            sharedPreferences.edit().clear().apply()

            // Hiển thị thông báo và chuyển người dùng về màn hình đăng nhập
            Toast.makeText(this, "Tài khoản đã được xóa", Toast.LENGTH_SHORT).show()

            // Chuyển đến màn hình đăng nhập và xóa stack hoạt động để người dùng không thể quay lại
            val intent = Intent(this, UI_Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Mật khẩu không đúng, không thể xóa tài khoản", Toast.LENGTH_SHORT).show()
        }
    }
}