package com.example.myproject.View

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.example.myproject.databinding.FragmentDictionarySearchBinding


class DictionarySearchFragment : Fragment() {
    private var _binding : FragmentDictionarySearchBinding? = null
    private  val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDictionarySearchBinding.inflate(inflater, container, false)
        return binding.root

        events()
    }

    private fun events() {
        binding.btnSearch.setOnClickListener{

        }

//        binding.searchInput.setOnEditorActionListener { _, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event?.keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN)) {
//                performSearch()
//
//            }
//        }
    }

    private fun performSearch() {
        val query = binding.searchInput.text.toString().trim()

        if (query.isEmpty()) {
            showToast("Vui lòng nhập từ cần tìm kiếm")
            return
        }
        // Ẩn bàn phím
        hideKeyboard()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // tránh memory leak
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}