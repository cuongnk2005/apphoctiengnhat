package com.example.myproject.View

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myproject.Model.AnkiFlashCard
import com.example.myproject.Model.FlashcardModel
import com.example.myproject.Model.ToastType
import com.example.myproject.R
import com.example.myproject.ViewModel.FormAddFlashCardViewModel
import com.example.myproject.databinding.ActivityFormAddFlashCardBinding

class FormAddFlashCard : AppCompatActivity() {
    private lateinit var binding: ActivityFormAddFlashCardBinding
    private val formAddFlashCardViewModel : FormAddFlashCardViewModel by viewModels()
    private var nameAnki = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFormAddFlashCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        formAddFlashCardViewModel.getBo()
        observeViewModel()
        event()
    }

    private fun observeViewModel(){
        formAddFlashCardViewModel.bo.observe(this){
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, it)
            binding.autoVocabSet.setAdapter(adapter)
            binding.autoVocabSet.setOnItemClickListener { _, _, position, _ ->
                val selectedVocab = it[position]
                nameAnki = selectedVocab
                showCustomToast(
                    context = this,
                    title = "Notification",
                    message = "Selected: $selectedVocab!",
                    type = ToastType.INFO
                )
//                Toast.makeText(this, "Đã chọn: $selectedVocab", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun event(){
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnSave.setOnClickListener {
            if(nameAnki.isEmpty()){
                showCustomToast(
                    context = this,
                    title = "Notification",
                    message = "No flashcard set selected",
                    type = ToastType.INFO
                )
//                Toast.makeText(this, "Chưa chọn bộ thẻ", Toast.LENGTH_SHORT).show()
            }
            val front = binding.editFront.text.toString()
            val back = binding.editBack.text.toString()
            val example = binding.editExample.text.toString()
            val tags = binding.editTags.text.toString()
            if(front.isEmpty() || back.isEmpty()){
                showCustomToast(
                    context = this,
                    title = "Notification",
                    message = "Please enter the front and back information!",
                    type = ToastType.WARNING
                )
//                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else{
                val flashCard = AnkiFlashCard("",front,back,example,tags)
                formAddFlashCardViewModel.pushFlashCardIntoAnki(nameAnki,flashCard)
                showCustomToast(
                    context = this,
                    title = "Notification",
                    message = "Card added successfully!",
                    type = ToastType.SUCCESS
                )
                clearText()
            }

            binding.btnCancel.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

        }
    }
    private fun clearText(){
        binding.editFront.setText("")
        binding.editBack.setText("")
        binding.editExample.setText("")
        binding.editTags.setText("")
    }

    private fun showCustomToast(context: Context, title: String, message: String, type: ToastType) {
        val layout = LayoutInflater.from(context).inflate(R.layout.custom_toast, null)

        layout.findViewById<TextView>(R.id.toast_title).text = title
        layout.findViewById<TextView>(R.id.toast_message).text = message

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

}