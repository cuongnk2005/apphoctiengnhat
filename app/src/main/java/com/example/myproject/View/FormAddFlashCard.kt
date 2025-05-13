package com.example.myproject.View

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.Model.AnkiFlashCard
import com.example.myproject.Model.FlashcardModel
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
                Toast.makeText(this, "Đã chọn: $selectedVocab", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun event(){
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnSave.setOnClickListener {
            if(nameAnki.isEmpty()){
                Toast.makeText(this, "Chưa chọn bộ thẻ", Toast.LENGTH_SHORT).show()
            }
            val front = binding.editFront.text.toString()
            val back = binding.editBack.text.toString()
            val example = binding.editExample.text.toString()
            val tags = binding.editTags.text.toString()
            if(front.isEmpty() || back.isEmpty() || example.isEmpty() || tags.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else{
                val flashCard = AnkiFlashCard(1,front,back,example,tags)
                formAddFlashCardViewModel.pushFlashCardIntoAnki(nameAnki,flashCard)
                Toast.makeText(this, "Thêm thẻ thành công", Toast.LENGTH_SHORT).show()
//                onBackPressedDispatcher.onBackPressed()
            }

        }
    }

}