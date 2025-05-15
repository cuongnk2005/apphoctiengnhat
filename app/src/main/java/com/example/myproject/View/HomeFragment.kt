package com.example.myproject.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myproject.Adapter.Home_Adapter
import com.example.myproject.Model.User
import com.example.myproject.R
import com.example.myproject.ViewModel.HomeViewmodel
import com.example.myproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adaterLervocabulary: Home_Adapter
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
        setAvatar()
        setupRecyclerView()
        homeviewModel.fetchTopics()
        observeViewModel()
        homeviewModel.updatepercen()


        // set Action cho image, để chuyển sang profile
        binding.avatar.setOnClickListener {
                val intent = Intent(requireContext(), Profile::class.java)
                startActivity(intent)
        }
        binding.btnLearn.setOnClickListener{
            val activity = binding.root.context
            if (activity is MainActivity) {
                activity.loadLearFragment(LearnVocabularyFragment())
            }
        }

    }

    private fun setupRecyclerView() {
        adaterLervocabulary = Home_Adapter()
        binding.lessonsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.lessonsRecyclerView.adapter = adaterLervocabulary
        adaterLervocabulary.onItemClick = { position ->
            val intent = Intent(requireContext(), LearnByFlashcard::class.java)
            intent.putExtra("FLASHCARD_SET_ID", homeviewModel.getTopicByposition(position)?.id)
            startActivity(intent)
        }
    }
    private fun setAvatar(){
        Log.d("testxde", "Đã gọi hàm fragment")
        homeviewModel.getUser { user: User? ->
            if (user != null  && _binding != null) {
                homeviewModel.setUserData(user)
                Glide.with(this)
                    .load(user.url)
                    .centerCrop()
                    .error(R.drawable.avatar)
                    .into(binding.avatar)
            }
        }
    }
    private fun observeViewModel() {
        homeviewModel.topics.observe(viewLifecycleOwner) { topic ->
            topic?.let{
                adaterLervocabulary.updateData(topic)
            }

        }
        homeviewModel.progressText.observe(viewLifecycleOwner) { progressText ->
            binding.progressText.text = progressText
        }

        // Observe progress percentage
        homeviewModel.progressPercentage.observe(viewLifecycleOwner) { percentage ->
            binding.progressBar.progress = percentage
            binding.loadingProgressBar.visibility = View.GONE
            binding.mainContentLayout.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // tránh memory leak
    }

}