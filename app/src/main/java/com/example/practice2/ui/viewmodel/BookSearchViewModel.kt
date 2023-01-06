package com.example.practice2.ui.viewmodel

import androidx.lifecycle.*
import com.example.practice2.data.model.SearchResponse
import com.example.practice2.data.repository.BookSearchRepository
import kotlinx.coroutines.Dispatchers
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
}