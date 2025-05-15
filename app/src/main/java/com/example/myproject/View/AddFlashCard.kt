package com.example.myproject.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.Adapter.AnkiAdapter
import com.example.myproject.Adapter.Home_Adapter
import com.example.myproject.Model.ToastType
import com.example.myproject.R
import com.example.myproject.ViewModel.AddFlashCardViewModel
import com.example.myproject.databinding.ActivityAddFlashCardBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText


class AddFlashCard : AppCompatActivity() {
    private lateinit var binding:ActivityAddFlashCardBinding
    private var isFabMenuOpen = false
    private val addFlashCardViewmodel : AddFlashCardViewModel by viewModels()
    private lateinit var ankiAdapter : AnkiAdapter
    private var currentIndex = 0
    private var blueCount = 0
    private var redCount = 0
    private var greenCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddFlashCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.toolbar)
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
        setupSwipeToDelete()
        ankiAdapter.onItemClick = { position ->
            val intent = Intent(this, FlashCardForMe::class.java)
            var name = addFlashCardViewmodel.getAnkiByPosition(position);
            Log.d("eenameeee", " co chay nayy $name")
            intent.putExtra("FLASHCARD_SET_Name", addFlashCardViewmodel.getAnkiByPosition(position))
            startActivity(intent)
        }
    }

    private fun setupSwipeToDelete() {
        val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            private val deleteIcon = ContextCompat.getDrawable(
                this@AddFlashCard,
                R.drawable.ic_delete // Hãy đảm bảo bạn có ic_delete trong thư mục drawable
            )

            private val background = ColorDrawable(Color.RED)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // Không hỗ trợ di chuyển mục
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedItem = addFlashCardViewmodel.getAnkiByPosition(position)

                // Xóa khỏi ViewModel
                addFlashCardViewmodel.removeFlashcardFromAnki(deletedItem)

                // Cập nhật lại RecyclerView bằng cách gọi getBo()
                addFlashCardViewmodel.getBo()

                // Hiển thị Snackbar cho phép người dùng hoàn tác
                Snackbar.make(binding.rvFlashcards, "Đã xóa '$deletedItem'", Snackbar.LENGTH_LONG).apply {
                    view.translationY = (-70f)
                }
                    .setAction("Hoàn tác") {
                        // Thêm lại mục đã xóa
                        addFlashCardViewmodel.addFlashcardIntoAnki(deletedItem)
                        addFlashCardViewmodel.getBo()
                    }
                    .show()

            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top

                // Vẽ background đỏ
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                background.draw(c)

                // Vẽ biểu tượng thùng rác
                deleteIcon?.let {
                    val intrinsicWidth = it.intrinsicWidth
                    val intrinsicHeight = it.intrinsicHeight

                    val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                    val deleteIconTop = itemView.top + deleteIconMargin
                    val deleteIconBottom = deleteIconTop + intrinsicHeight
                    val deleteIconRight = itemView.right - deleteIconMargin
                    val deleteIconLeft = deleteIconRight - intrinsicWidth

                    it.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                    it.draw(c)
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        // Gắn ItemTouchHelper vào RecyclerView
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvFlashcards)
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



//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_delete -> {
//
//                true
//            }
//            R.id.action_check -> {
//
//                true
//            }
//            R.id.action_manage -> {
//
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

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
//        Toast.makeText(this, "Nhận bộ thẻ đã chia sẻ", Toast.LENGTH_SHORT).show()
        showCustomToast(
            context = this,
            title = "Thông báo",
            message = "Nhận bộ thẻ đã chia sẻ",
            type = ToastType.INFO
        )
    }

    private fun handleFilterDecks() {
        // Xử lý logic tạo bộ thẻ đã lọc
        showCustomToast(
            context = this,
            title = "Thông báo",
            message = "Tạo bộ thẻ đã lọc",
            type = ToastType.INFO
        )
//        Toast.makeText(this, "Tạo bộ thẻ đã lọc", Toast.LENGTH_SHORT).show()
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
                    showCustomToast(
                        context = this,
                        title = "Thông báo",
                        message = "Thêm bộ thẻ $inputText thành công!",
                        type = ToastType.SUCCESS
                    )
//                    Toast.makeText(this, "Thêm bộ thẻ $inputText", Toast.LENGTH_SHORT).show()
                    addFlashCardViewmodel.getBo()
                } else{
                    showCustomToast(
                        context = this,
                        title = "Thông báo",
                        message = "Thêm bộ thẻ $inputText thất bại!",
                        type = ToastType.ERROR
                    )
//                    Toast.makeText(this, "Thêm thất bại $inputText", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun handleAddItem() {
        // Xử lý logic thêm
        val intent = Intent(this ,FormAddFlashCard::class.java)
        startActivity(intent)
        showCustomToast(
            context = this,
            title = "Thông báo",
            message = "Đã chuyển vào trang thêm",
            type = ToastType.INFO
        )
//        Toast.makeText(this, "Thêm", Toast.LENGTH_SHORT).show()
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