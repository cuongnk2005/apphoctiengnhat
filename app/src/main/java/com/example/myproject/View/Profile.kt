package com.example.myproject.View

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.myproject.Model.CloudinaryHelper
import com.example.myproject.Model.User
import com.example.myproject.R
import com.example.myproject.Repository.AuthRepository
import com.example.myproject.ViewModel.ProfileModel
import com.example.myproject.databinding.ActivityProfileBinding
import com.google.android.material.textfield.TextInputEditText

class Profile : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    private  var uriImage: Uri? = null
    private var url:String = ""
    private var username:String = ""
    private var password:String = ""
    private var userfromIntent:User? = null
    private val profileModel: ProfileModel by viewModels()
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
        CloudinaryHelper.init(this)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        writeInformation()
        events()
    }

    private fun writeInformation(){
        userfromIntent = intent.getSerializableExtra("user121") as? User

        userfromIntent?.let {
            profileModel.setUserData(it)
            profileModel.user.observe(this){user ->
            binding.nameEditText.setText(it.username)
            binding.emailEditText.setText(it.gmail)
            if(user.url!= null){
                Log.d("Url tu user", "${user.url}")
                Glide.with(this)
                    .load(user.url)
                    .centerCrop()
                    .error(R.drawable.avatar)
                    .into(binding.profileImage)
            }
            }
        }

    }
    private fun events() {
        // back lại trang home
        binding.backButton.setOnClickListener{
            AlertDialog.Builder(this)
                .setTitle("Xác nhận lưu")
                .setPositiveButton("Xác nhận") { dialog, _ ->

                    if(uriImage == null){
                        val updatemap = mapOf<String, Any>(
                            "username" to binding.nameEditText.text.toString(),
                        )
                        profileModel.updateUserByID(updatemap)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }else {
                        uploadToCloudinary(uriImage!!, {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        })
                    }

                    Log.d("Urltest", url)

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
         binding.nameEditText.setText(name)
        Log.d("testname", "${binding.nameEditText.text.toString()}")

    }


    // hàm upload anh bang cloudinary
    private fun uploadToCloudinary(uri: Uri , callback:() -> Unit){

            val uploadPreset = "Lear_nihongo"
            com.cloudinary.android.MediaManager.get().upload(uri)
                .unsigned(uploadPreset)
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String?) {
                        Log.d("Cloudinary", "Upload bat dau")
                    }

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {

                    }

                    override fun onSuccess(
                        requestId: String?,
                        resultData: MutableMap<Any?, Any?>?
                    ) {
                        resultData?.let { data ->
                            // Lấy giá trị "url" và cast sang String
                            url = (data["url"] as? String).toString().replace("http://", "https://")

                            val updatemap = mapOf<String, Any>(

                                "username" to binding.nameEditText.text.toString(),
                                "url" to url
                            )
                            profileModel.updateUserByID(updatemap)
                            Toast.makeText(
                                this@Profile,
                                "cập nhật thông tin thành công ",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.d("sucesss", "upload Thanh cong$url")
                            callback()
                        }

                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        Toast.makeText(this@Profile, "Lỗi: ${error.toString()}", Toast.LENGTH_LONG)
                            .show()
                        Log.e("loicloud", "Lỗi: ${error.toString()}")
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {

                    }
                })
                .dispatch()

    }
    // update user
//    suspend fun updateUser() {
//        delay(2000)
//        val updatemap = mapOf<String, Any>(
//            "username" to binding.nameEditText.text.toString(),
//            "url" to url
//        )
//        profileModel.updateUserByID(updatemap)
//    }

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
                    newPassword.length < 6 -> {
                        Toast.makeText(this, "Vui lòng nhập mật khẩu mới trên 5 kí tự", Toast.LENGTH_SHORT).show()
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
         profileModel.changePassword(currentPassword, newPassword){
             showSusscessDialog(this, it)
         }
    }
    private fun showSusscessDialog(context: Context, errorMessage: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Thông Báo")
        builder.setMessage(errorMessage)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
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