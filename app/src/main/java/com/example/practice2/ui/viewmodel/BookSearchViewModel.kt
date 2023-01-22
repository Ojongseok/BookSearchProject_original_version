package com.example.practice2.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.practice2.data.model.Book
import com.example.practice2.data.model.SearchResponse
import com.example.practice2.data.repository.BookSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookSearchViewModel(private val bookSearchRepository: BookSearchRepository) : ViewModel() {
    private val _searchResult = MutableLiveData<SearchResponse>()
    val searchResult : LiveData<SearchResponse> get() = _searchResult

    fun searchBooks(query : String) = viewModelScope.launch(Dispatchers.IO) {
        val response = bookSearchRepository.searchBooks(query,getSortMode(),1,15)
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

    // DataStore
    fun savaSortMode(value: String) = viewModelScope.launch(Dispatchers.IO) {
        bookSearchRepository.saveSortMode(value)
    }

    suspend fun getSortMode() = withContext(Dispatchers.IO) {
        bookSearchRepository.getSortMode().first()
    }

    // Paging
    val favoritePagingBooks: StateFlow<PagingData<Book>> =
        bookSearchRepository.getFavoritePagingBooks()
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())
}