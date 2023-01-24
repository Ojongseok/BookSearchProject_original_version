package com.example.practice2.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practice2.databinding.FragmentFavoriteBinding
import com.example.practice2.ui.adapter.BookSearchPagingAdapter
import com.example.practice2.ui.viewmodel.BookSearchViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment() {
    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BookSearchViewModel
//    private lateinit var bookSearchAdapter : BookSearchAdapter
    private lateinit var bookSearchAdapter: BookSearchPagingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()
        setupTouchHelper(view)

//        viewModel.favoriteBooks.observe(viewLifecycleOwner) {
//            bookSearchAdapter.submitList(it)
//        }

        // cold 스트림일 떄
//        lifecycleScope.launch {
//            viewModel.favoriteBooks.collectLatest {
//                bookSearchAdapter.submitList(it)
//            }
//        }

        // hot 스트림일 때
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.favoriteBooks.collectLatest {
//                    bookSearchAdapter.submitList(it)
//                }
//            }
//        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoritePagingBooks.collectLatest {
                    bookSearchAdapter.submitData(it)
                }
            }
        }

    }

    fun setupRecyclerView() {
//        bookSearchAdapter = BookSearchAdapter()
        bookSearchAdapter = BookSearchPagingAdapter()
        binding.rvFavoriteBooks.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            addItemDecoration(
                DividerItemDecoration(requireContext(),
                    DividerItemDecoration.VERTICAL)
            )
            adapter = bookSearchAdapter
        }
        bookSearchAdapter.setOnItemClickListener {
            val action = FavoriteFragmentDirections.actionFragmentFavoriteToFragmentBook(it)
            findNavController().navigate(action)
        }
    }

    private fun setupTouchHelper(view:View) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
//                val book = bookSearchAdapter.currentList[position]
//                viewModel.deleteBook(book)
//                Snackbar.make(view, "Book 삭제", Snackbar.LENGTH_SHORT).apply {
//                    setAction("취소"){
//                        viewModel.saveBook(book)
//                    }.show()
//                }
                val pagedBook = bookSearchAdapter.peek(position)
                pagedBook?.let { book ->
                    viewModel.deleteBook(book)
                    Snackbar.make(view, "Book 삭제", Snackbar.LENGTH_SHORT).apply {
                        setAction("취소"){
                            viewModel.saveBook(book)
                        }.show()
                    }
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvFavoriteBooks)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}