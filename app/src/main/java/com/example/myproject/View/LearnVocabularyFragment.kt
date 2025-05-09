package com.example.myproject.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.Adapter.LearnVocabulary_Adapter
import com.example.myproject.R
import com.example.myproject.ViewModel.LearVocabularyViewModel
import com.example.myproject.databinding.FragmentLearnVocabularyBinding
import com.google.android.material.chip.Chip


class LearnVocabularyFragment : Fragment() {
    private var _binding : FragmentLearnVocabularyBinding? = null
    private val binding get() = _binding!!
    private var selectedCategory : String = "all"
    private lateinit  var adapterLear: LearnVocabulary_Adapter
    private val viewModel: LearVocabularyViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLearnVocabularyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycleView()
        viewModel.fetchTopics()
        viewModel.getListIdOldTopic()
        observeViewModel()
        events()
        setupUI()
    }

    private fun setupUI() {
        setupChips()


    }

//    private fun setupChips() {
//        // Mặc định chip "Tất cả" được chọn
//        binding.chipAll.isChecked = true
//
//        // Thiết lập sự kiện khi chọn chip
//        binding.chipAll.setOnClickListener { handleChipSelection(it as Chip, "all") }
//        binding.chipFood.setOnClickListener { handleChipSelection(it as Chip, "food") }
//        binding.chipAnimals.setOnClickListener { handleChipSelection(it as Chip, "animals") }
//        binding.chipTransport.setOnClickListener { handleChipSelection(it as Chip, "transport") }
//        binding.chipNature.setOnClickListener { handleChipSelection(it as Chip, "nature") }
//    }
//
//    private fun handleChipSelection(selectedChip: Chip, category: String) {
//        // Bỏ chọn tất cả các chip
//        binding.chipAll.isChecked = false
//        binding.chipFood.isChecked = false
//        binding.chipAnimals.isChecked = false
//        binding.chipTransport.isChecked = false
//        binding.chipNature.isChecked = false
//
//        // Chọn chip hiện tại
//        selectedChip.isChecked = true
//
//        // Lưu lại chủ đề đã chọn
//        selectedCategory = category
//
//    }
private fun setupChips() {
    // Danh sách các chip và category tương ứng
    val chipList = listOf(
        binding.chipAll to "all",
        binding.chipFood to "food",
        binding.chipAnimals to "animals",
        binding.chipTransport to "transport",
        binding.chipNature to "nature"
    )

    // Thiết lập sự kiện cho mỗi chip
    chipList.forEach { (chip, category) ->
        chip.setOnClickListener {
            selectChip(chip, category)
        }
    }

    // Mặc định chọn chip đầu tiên (All)
    selectChip(binding.chipAll, "all")
}

    private fun selectChip(selectedChip: Chip, category: String) {
        // Danh sách tất cả chip
        val allChips = listOf(
            binding.chipAll,
            binding.chipFood,
            binding.chipAnimals,
            binding.chipTransport,
            binding.chipNature
        )

        // Bỏ chọn tất cả chip
        allChips.forEach { chip ->
            chip.isChecked = false
        }

        // Chọn chip hiện tại
        selectedChip.isChecked = true

        // Lưu lại category đã chọn
        selectedCategory = category

        // Lọc danh sách từ vựng theo category
        filterVocabularyByCategory(category)

        // Ẩn bàn phím nếu đang hiển thị
        hideKeyboard()
    }

    private fun filterVocabularyByCategory(category: String) {
        // Triển khai logic lọc danh sách từ vựng theo category
        // Đây là nơi bạn sẽ gọi đến ViewModel để lấy dữ liệu theo category
        viewModel.filterTopicsByCategory(category)
    }

    private fun events() {
        binding.btnBack.setOnClickListener {
//            val intent = Intent(requireContext(), HomeFragment::class.java)
//            startActivity(intent)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setUpRecycleView(){
        adapterLear = LearnVocabulary_Adapter()
        binding.wordRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.wordRecyclerView.adapter = adapterLear
        adapterLear.onItemClick = { position ->
            val intent = Intent(requireContext(), LearnByFlashcard::class.java)
            val id:String = viewModel.getTopicByposition(position)?.id.toString()
            viewModel.changeListIdOldTopic(id)
            intent.putExtra("FLASHCARD_SET_ID", id)

            startActivity(intent)
        }
    }
   private fun observeViewModel(){
    viewModel.topics.observe(viewLifecycleOwner){ topics ->
        adapterLear?.updateData(topics)
    }
     viewModel.ListIdOldTopic.observe(viewLifecycleOwner){
         viewModel.updateUser()
     }
   }

    // Hàm ẩn bàn phím khi cần
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.let { v ->
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // tránh memory leak
    }
}