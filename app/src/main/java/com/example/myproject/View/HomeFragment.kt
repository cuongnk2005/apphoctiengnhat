package com.example.myproject.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.Adapter.LearnVocabulary_Adapter
import com.example.myproject.R
import com.example.myproject.ViewModel.HomeViewmodel
import com.example.myproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adaterLervocabulary: LearnVocabulary_Adapter
    private val homeviewModel: HomeViewmodel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }
    //savedInstanceState: Bundle? giữ trạng thái khi chuyển view
//onViewCreated là hàm kế thừa dùng viet các hàm tương tác với frame
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        homeviewModel.fetchTopics()
        observeViewModel()
    }
    private fun setupRecyclerView() {
        adaterLervocabulary = LearnVocabulary_Adapter()
        binding.lessonsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.lessonsRecyclerView.adapter = adaterLervocabulary
    }
    private fun observeViewModel() {
        homeviewModel.topics.observe(viewLifecycleOwner) { topic ->
            topic?.let{
                adaterLervocabulary.updateData(topic)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // tránh memory leak
    }
}