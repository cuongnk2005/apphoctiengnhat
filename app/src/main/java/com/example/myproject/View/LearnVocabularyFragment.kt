package com.example.myproject.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myproject.R
import com.example.myproject.databinding.FragmentLearnVocabularyBinding


class LearnVocabularyFragment : Fragment() {
    private var _binding : FragmentLearnVocabularyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLearnVocabularyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // tránh memory leak
    }
}