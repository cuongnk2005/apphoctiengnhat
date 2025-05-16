package com.example.myproject.ViewModel

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.Model.AnkiFlashCard
import com.example.myproject.Model.AnkiScheduler
import com.example.myproject.Model.ToastType
import com.example.myproject.R
import com.example.myproject.Repository.AnkiRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class LearAnkiFlashCardModel: ViewModel() {
    private val ankiRepository = AnkiRepository()
    private val _ankiflashcard = MutableLiveData<List<AnkiFlashCard>>()
    val ankiFlashCard : LiveData<List<AnkiFlashCard>> get() = _ankiflashcard
    private var _currentCard = MutableLiveData<AnkiFlashCard?>()
    val currentCard : LiveData<AnkiFlashCard?> get() = _currentCard
    private var currentpoint = 0
    private var listFlashCard = mutableListOf<AnkiFlashCard>()

    private var ankiScheduler = AnkiScheduler()
    private var nameAnki = ""
    fun getAnkiFlashCardByName(name: String){
        this.nameAnki = name
        viewModelScope.launch {
            _ankiflashcard.value = ankiRepository.getAnkiFlasCardByNameForLear(name)
            val now = LocalDateTime.now()
            listFlashCard = _ankiflashcard.value?.sortedWith(compareBy(
                { LocalDateTime.parse(it.nextReviewDate).isAfter(now.plusMinutes(1)) },
                { LocalDateTime.parse(it.nextReviewDate).isAfter(now.plusMinutes(10)) },
                { LocalDateTime.parse(it.nextReviewDate) }
            ))?.toMutableList() ?: mutableListOf()
            Log.d("jkfsf", "${listFlashCard.size}")
            if (listFlashCard.isNotEmpty()) {
                _currentCard.value = listFlashCard[currentpoint]
            } else {
                _currentCard.value = null
            }
        }
    }
    fun getNextCurrentCard() {
        if (listFlashCard.isNotEmpty()) {
            currentpoint++
            if (currentpoint >= listFlashCard.size) {
                currentpoint = 0
            }
            _currentCard.value = listFlashCard[currentpoint]
        }
    }
    fun updateCurrentFlashCard(card:AnkiFlashCard, rating: Int){
        Log.d("sjdkhvjk", "co chay ham nay")
        val newCard = ankiScheduler.reviewCard(card,rating)
        Log.d("sjdkhvjsdfsk", "co chay ham nay")
        listFlashCard.removeAt(currentpoint)
        listFlashCard.add(currentpoint,newCard)
        Log.d("ssdfsk", "co chay ham nay")

        Log.d("srgsfasdfsk", "co chay ham nay")
        ankiRepository.UpdateAnkiFlasCardByName(nameAnki,newCard)
        getAnkiFlashCardByName(nameAnki)


    }
    fun deleteFlashCard(){
        Log.d("sjdkhvjk", "${_currentCard.value?.id.toString()}")
        _currentCard.value?.id?.let { ankiRepository.deleteAnkiFlashCard(it, this.nameAnki) }
        getAnkiFlashCardByName(nameAnki)
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
}