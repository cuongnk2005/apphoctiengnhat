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
import com.google.android.material.chip.ChipGroup


class LearnVocabularyFragment : Fragment() {
    private var _binding: FragmentLearnVocabularyBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterLear: LearnVocabulary_Adapter
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
//        setupChips()
        creatChip()

    }


    private fun events() {
        binding.btnBack.setOnClickListener {
//            val intent = Intent(requireContext(), HomeFragment::class.java)
//            startActivity(intent)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setUpRecycleView() {
        adapterLear = LearnVocabulary_Adapter()
        binding.wordRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.wordRecyclerView.adapter = adapterLear
        adapterLear.onItemClick = { position ->
            val intent = Intent(requireContext(), LearnByFlashcard::class.java)
            val id: String = viewModel.getTopicByposition(position)?.id.toString()
            viewModel.changeListIdOldTopic(id)
            intent.putExtra("FLASHCARD_SET_ID", id)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.topics.observe(viewLifecycleOwner) { topics ->


            adapterLear.submitList(topics)
        }
        viewModel.ListIdOldTopic.observe(viewLifecycleOwner) {
            viewModel.updateUser()
        }
    }

    // Hàm ẩn bàn phím khi cần
    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.let { v ->
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // tránh memory leak
    }

    fun creatChip() {
        viewModel.getTheme()
        viewModel.theme.observe(viewLifecycleOwner) { themes ->



                val chipGroup = binding.chipGroup
            chipGroup.removeAllViews()
            themes.forEachIndexed { index, theme ->
                    val chip = Chip(requireContext()).apply {
                        text = theme
                        isCheckable = true
                        isChecked = index == 0 // Chip đầu tiên được chọn mặc định
                        setChipBackgroundColorResource(R.color.chip_background_selector)
                        layoutParams = ViewGroup.MarginLayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply {
                            marginEnd =
                                resources.getDimensionPixelSize(R.dimen.chip_margin_end) // tạo dimen chip_margin_end trong res
                        }
                        setOnClickListener {
                            if (index == 0) {
                                viewModel.fetchTopics()

                            } else {
                             viewModel.fetchTopicsByTheme(theme)
                            }
                        }
                    }
                    chipGroup.addView(chip)
                }
            }
    }
}