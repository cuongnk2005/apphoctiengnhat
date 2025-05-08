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


class LearnVocabularyFragment : Fragment() {
    private var _binding : FragmentLearnVocabularyBinding? = null
    private val binding get() = _binding!!
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