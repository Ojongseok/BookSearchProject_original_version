package com.example.practice2.ui.view

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practice2.databinding.FragmentSearchBinding
import com.example.practice2.ui.adapter.BookSearchAdapter
import com.example.practice2.ui.viewmodel.BookSearchViewModel

class SearchFragment : Fragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var bookSearchViewModel : BookSearchViewModel
    private lateinit var bookSearchAdapter : BookSearchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookSearchViewModel = (activity as MainActivity).viewModel

        setupRecyclerView()
        searchBooks()

        bookSearchViewModel.searchResult.observe(viewLifecycleOwner) {
            val books = it.documents
            bookSearchAdapter .submitList(books)
        }
    }
    fun setupRecyclerView() {
        bookSearchAdapter = BookSearchAdapter()
        binding.rvSearchResult.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
            adapter = bookSearchAdapter
        }
    }
    fun searchBooks() {
        var startTime = System.currentTimeMillis()
        var endTime : Long

        binding.etSearch.addTextChangedListener { text: Editable? ->
            endTime = System.currentTimeMillis()
            if (endTime - startTime >= 100L) {
                text?.let {
                    val query = it.toString().trim()
                    if (query.isNotEmpty()) {
                        bookSearchViewModel.searchBooks(query)
                    }
                }
            }
            startTime = endTime
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}