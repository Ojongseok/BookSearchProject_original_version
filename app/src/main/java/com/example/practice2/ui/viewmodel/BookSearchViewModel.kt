package com.example.practice2.ui.viewmodel

import androidx.lifecycle.*
import com.example.practice2.data.model.Book
import com.example.practice2.data.model.SearchResponse
import com.example.practice2.data.repository.BookSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookSearchViewModel(private val bookSearchRepository: BookSearchRepository) : ViewModel() {
    private val _searchResult = MutableLiveData<SearchResponse>()
    val searchResult : LiveData<SearchResponse> get() = _searchResult

    fun searchBooks(query : String) = viewModelScope.launch(Dispatchers.IO) {
        val response = bookSearchRepository.searchBooks(query,"accuracy",1,15)
        if (response.isSuccessful) {
            response.body()?.let {
                _searchResult.postValue(it)
            }
        }
    }
    // Room
    fun saveBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.insertBooks(book)
    }
    fun deleteBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.deleteBooks(book)
    }
    // cold 스트림
//    val favoriteBooks: Flow<List<Book>> = bookSearchRepository.getFavoriteBooks()

    // hot 스트림
    val favoriteBooks: StateFlow<List<Book>> = bookSearchRepository.getFavoriteBooks()
        .stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000), listOf())
}