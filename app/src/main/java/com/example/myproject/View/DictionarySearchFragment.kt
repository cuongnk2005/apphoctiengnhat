package com.example.myproject.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.myproject.Model.DictionaryEntry
import com.example.myproject.R
import com.example.myproject.Repository.DictionaryRepository
import com.example.myproject.ViewModel.DictionaryViewModel
import com.example.myproject.ViewModel.DictionaryViewModelFactory
import com.example.myproject.databinding.FragmentDictionarySearchBinding

class DictionarySearchFragment : Fragment() {

    private var _binding: FragmentDictionarySearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DictionaryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDictionarySearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = DictionaryRepository(requireContext())
        val factory = DictionaryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[DictionaryViewModel::class.java]

        setupEvents()
        observeViewModel()
    }

    private fun setupEvents() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
//            val intent = Intent(requireContext(), HomeFragment::class.java)
//            startActivity(intent)
        }

        binding.btnSearch.setOnClickListener {
            performSearch()
        }

        binding.btnClear.setOnClickListener {
            binding.searchInput.text.clear()
            showEmptyState()
        }

        binding.btnFavorite.setOnClickListener {
            Toast.makeText(requireContext(), "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show()
        }

        binding.btnAudioResult.setOnClickListener {
            Toast.makeText(requireContext(), "Chức năng phát âm đang được phát triển", Toast.LENGTH_SHORT).show()
        }


        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnClear.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.searchInput.setOnEditorActionListener { _, _, _ ->
            performSearch()
            true
        }
    }

    private fun observeViewModel() {
        viewModel.wordResult.observe(viewLifecycleOwner) { entry ->
            if (entry != null) {
                updateResultCard(entry)
                showResultCard()
            } else {
                showEmptyState()
                Toast.makeText(requireContext(), "Không tìm thấy từ này", Toast.LENGTH_SHORT).show()
            }
        }

        // hien thi loading
        val loadingLayout = view?.findViewById<LinearLayout>(R.id.loadingLayout)
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (loadingLayout != null) {
                loadingLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
            }


        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
    }

    private fun performSearch() {
        val query = binding.searchInput.text.toString().trim()
        if (query.isNotEmpty()) {
            viewModel.searchWord(query)
        } else {
            Toast.makeText(requireContext(), "Vui lòng nhập từ cần tra cứu", Toast.LENGTH_SHORT).show()
        }
        hideKeyboard()
    }

    private fun updateResultCard(entry: DictionaryEntry) {
        binding.resultWord.text = entry.word
        binding.resultPronunciation.text = entry.reading
        binding.wordType.text = entry.partOfSpeech
        binding.wordTitle.text = "1. ${entry.meaning}"
        binding.wordDecribe.text = entry.explanation

        if (entry.example.isNotEmpty()) {
            binding.wordExample.visibility = View.VISIBLE
            binding.wordMean.visibility = View.VISIBLE
            binding.wordExample.text = "Ví dụ: ${entry.example}"
            binding.wordMean.text = entry.exampleMeaning
        } else {
            binding.wordExample.visibility = View.GONE
            binding.wordMean.visibility = View.GONE
        }
    }

    private fun showResultCard() {
        binding.emptyState.visibility = View.GONE
        binding.resultCard.visibility = View.VISIBLE
    }

    private fun showEmptyState() {
        binding.emptyState.visibility = View.VISIBLE
        binding.resultCard.visibility = View.GONE
    }

    // hàm ẩn bàn phím
    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}