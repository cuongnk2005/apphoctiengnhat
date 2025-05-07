package com.example.myproject.View

import android.content.Context
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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import com.example.myproject.Model.DictionaryEntry
import com.example.myproject.R
import com.example.myproject.Repository.DictionaryRepository
import com.example.myproject.databinding.FragmentDictionarySearchBinding


class DictionarySearchFragment : Fragment() {

    private var _binding: FragmentDictionarySearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: DictionaryRepository
    private var isJapaneseToVietnamese = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDictionarySearchBinding.inflate(inflater, container, false)
        repository = DictionaryRepository(requireContext())

        setupViews()
        setupListeners()

        return binding.root
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupListeners() {
        binding.btnSearch.setOnClickListener {
            performSearch()
        }

        binding.searchInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                performSearch()
                true
            } else false
        }

        binding.btnClear.setOnClickListener {
            binding.searchInput.setText("")
            it.visibility = View.GONE
            binding.resultCard.visibility = View.GONE
            binding.emptyState.visibility = View.VISIBLE
        }

        binding.searchInput.addTextChangedListener {
            binding.btnClear.visibility = if (it.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        binding.btnSwitch.setOnClickListener {
            isJapaneseToVietnamese = !isJapaneseToVietnamese
            if (isJapaneseToVietnamese) {
                binding.fromLanguage.text = "Tiếng Nhật"
                binding.toLanguage.text = "Tiếng Việt"
            } else {
                binding.fromLanguage.text = "Tiếng Việt"
                binding.toLanguage.text = "Tiếng Nhật"
            }
        }
    }

    private fun performSearch() {
        val query = binding.searchInput.text.toString().trim()

        if (query.isEmpty()) {
            showToast("Vui lòng nhập từ cần tìm kiếm")
            return
        }

        // an ban phim
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)

        val result = repository.searchWord(query, isJapaneseToVietnamese)

        if (result != null) {
            displayResult(result)
        } else {
            showToast("Không tìm thấy từ \"$query\"")
        }
    }

    private fun displayResult(entry: DictionaryEntry) {
        binding.resultCard.visibility = View.VISIBLE
        binding.emptyState.visibility = View.GONE

        binding.resultWord.text = entry.word
        binding.resultPronunciation.text = entry.reading
        binding.wordType.text = entry.wordType
        binding.wordTitle.text = entry.meaning
        binding.wordDecribe.text = entry.wordDecribe
        binding.wordExample.text = entry.wordExample
        binding.wordMean.text = entry.wordMean



        binding.btnFavorite.setOnClickListener {
            entry.isFavorite = !entry.isFavorite
            if (entry.isFavorite) {
                showToast("Đã thêm vào danh sách yêu thích")
                binding.btnFavorite.setImageResource(R.drawable.ic_heart_check)
            } else {
                showToast("Đã xóa khỏi danh sách yêu thích")
                binding.btnFavorite.setImageResource(R.drawable.ic_heart_plus)
            }

        }

        binding.btnAudioResult.setOnClickListener {
            showToast("Đang phát âm...")
        }

        binding.btnAddToList.setOnClickListener {
            showToast("Đã thêm vào danh sách học")
        }
    }

    private fun showToast(msg:String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}