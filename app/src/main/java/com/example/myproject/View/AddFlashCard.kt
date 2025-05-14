package com.example.myproject.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.Adapter.AnkiAdapter
import com.example.myproject.Adapter.Home_Adapter
import com.example.myproject.R
import com.example.myproject.ViewModel.AddFlashCardViewModel
import com.example.myproject.databinding.ActivityAddFlashCardBinding
import com.google.android.material.textfield.TextInputEditText


class AddFlashCard : AppCompatActivity() {
    private lateinit var binding:ActivityAddFlashCardBinding
    private var isFabMenuOpen = false
    private val addFlashCardViewmodel : AddFlashCardViewModel by viewModels()
    private lateinit var ankiAdapter : AnkiAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddFlashCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        events()
        setupFabEvents()
        setupRecyclerView()
        addFlashCardViewmodel.getBo()
        observeViewModel()
    }

    private fun setupRecyclerView(){
        ankiAdapter = AnkiAdapter()
        binding.rvFlashcards.layoutManager = LinearLayoutManager(this)
        binding.rvFlashcards.adapter = ankiAdapter
        ankiAdapter.onItemClick = { position ->
            val intent = Intent(this, FlashCardForMe::class.java)
            var name = addFlashCardViewmodel.getAnkiByPosition(position);
            Log.d("eenameeee", " co chay nayy $name")
            intent.putExtra("FLASHCARD_SET_Name", addFlashCardViewmodel.getAnkiByPosition(position))
            startActivity(intent)
        }
    }
    private fun observeViewModel(){
        addFlashCardViewmodel.bo.observe(this){
            ankiAdapter.updateData(it)
            binding.emptyState.visibility= View.GONE
        }

    }
    private fun events() {
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_of_add_flashcard, menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {

                true
            }
            R.id.action_check -> {

                true
            }
            R.id.action_manage -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // setup su kien
    private fun setupFabEvents() {
        // Sự kiện click FAB chính
        binding.fabMain.setOnClickListener {
            toggleFabMenu()
        }

        // Sự kiện click từng menu item
        binding.fabSharedDecks.setOnClickListener {
            handleSharedDecks()
            closeFabMenu()
        }

        binding.fabFilterDecks.setOnClickListener {
            handleFilterDecks()
            closeFabMenu()
        }

        binding.fabCreateDeck.setOnClickListener {
            handleCreateDeck()
            closeFabMenu()
        }

        binding.fabAddItem.setOnClickListener {
            handleAddItem()
            closeFabMenu()
        }

        // Đóng menu khi click overlay
        binding.fabOverlay.setOnClickListener {
            closeFabMenu()
        }
    }

    private fun toggleFabMenu() {
        if (isFabMenuOpen) {
            closeFabMenu()
        } else {
            openFabMenu()
        }
    }

    private fun openFabMenu() {
        isFabMenuOpen = true

        // Hiển thị overlay
        binding.fabOverlay.visibility = View.VISIBLE
        binding.fabOverlay.animate().alpha(0.5f).duration = 300

        // Animation các menu item
        val menuItems = listOf(
            binding.fabSharedDecks,
            binding.fabFilterDecks,
            binding.fabCreateDeck,
            binding.fabAddItem
        )

        // Xoay FAB chính
        binding.fabMain.animate()
            .rotation(45f)
            .setDuration(300)
            .start()

        // Animation cho từng menu item
        menuItems.forEachIndexed { index, fab ->
            fab.visibility = View.VISIBLE
            fab.translationY = 100f
            fab.alpha = 0f
            fab.animate()
                .translationY(0f)
                .alpha(1f)
                .setStartDelay((50 * index).toLong())
                .setDuration(300)
                .start()
        }
    }

    private fun closeFabMenu() {
        isFabMenuOpen = false

        // Ẩn overlay
        binding.fabOverlay.animate()
            .alpha(0f)
            .withEndAction { binding.fabOverlay.visibility = View.GONE }
            .duration = 300

        // Xoay FAB chính ngược lại
        binding.fabMain.animate()
            .rotation(0f)
            .setDuration(300)
            .start()

        // Animation các menu item
        val menuItems = listOf(
            binding.fabSharedDecks,
            binding.fabFilterDecks,
            binding.fabCreateDeck,
            binding.fabAddItem
        )

        menuItems.forEachIndexed { index, fab ->
            fab.animate()
                .translationY(100f)
                .alpha(0f)
                .setStartDelay((50 * index).toLong())
                .setDuration(300)
                .withEndAction { fab.visibility = View.GONE }
                .start()
        }
    }

    // Các hàm xử lý sự kiện cho từng menu item
    private fun handleSharedDecks() {
        // Xử lý logic nhận bộ thẻ đã chia sẻ
        Toast.makeText(this, "Nhận bộ thẻ đã chia sẻ", Toast.LENGTH_SHORT).show()
    }

    private fun handleFilterDecks() {
        // Xử lý logic tạo bộ thẻ đã lọc
        Toast.makeText(this, "Tạo bộ thẻ đã lọc", Toast.LENGTH_SHORT).show()
    }


    @SuppressLint("MissingInflatedId")
    private fun handleCreateDeck() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_tag, null)
        val nameTag = dialogView.findViewById<TextInputEditText>(R.id.etNameTag)

        AlertDialog.Builder(this)
            .setTitle("Thêm bộ từ vựng")
            .setView(dialogView)
            .setPositiveButton("Thêm") {dialog, _ ->
                val inputText = nameTag.text.toString().trim()
                if (inputText.isNotEmpty()){
                    addFlashCardViewmodel.addFlashcardIntoAnki(inputText)
                    Toast.makeText(this, "Thêm bộ thẻ $inputText", Toast.LENGTH_SHORT).show()
                    addFlashCardViewmodel.getBo()
                } else{
                    Toast.makeText(this, "Thêm thất bại $inputText", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun handleAddItem() {
        // Xử lý logic thêm
        val intent = Intent(this ,FormAddFlashCard::class.java)
        startActivity(intent)
        Toast.makeText(this, "Thêm", Toast.LENGTH_SHORT).show()
    }

}