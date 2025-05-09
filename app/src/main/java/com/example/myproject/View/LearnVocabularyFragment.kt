package com.example.myproject.View

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private fun setupChips() {
        binding.chipAll.isCheckable = true

        binding.chipAll.setOnClickListener {selectedChip(it as Chip, "all")}
        binding.chipFood.setOnClickListener { selectedChip(it as Chip, "food") }
        binding.chipAnimals.setOnClickListener { selectedChip(it as Chip, "animals") }
        binding.chipTransport.setOnClickListener { selectedChip(it as Chip, "transport") }
        binding.chipNature.setOnClickListener { selectedChip(it as Chip, "nature") }
    }

    private fun selectedChip(selectedChip: Chip, category: String) {
        binding.chipAll.isCheckable = false
        binding.chipFood.isChecked = false
        binding.chipAnimals.isChecked = false
        binding.chipTransport.isChecked = false
        binding.chipNature.isChecked = false
        selectedChip.isCheckable = true
        selectedCategory = category

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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // tránh memory leak
    }
}