package com.example.practice2.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practice2.data.model.Book
import com.example.practice2.data.model.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface BookSearchRepository {
    suspend fun searchBooks(
        query : String,
        sort : String,
        page : Int,
        size : Int
    ) : Response<SearchResponse>

    // Room
    suspend fun insertBooks(book: Book)

    suspend fun deleteBooks(book: Book)

    fun getFavoriteBooks(): Flow<List<Book>>

    suspend fun saveSortMode(mode: String)

    suspend fun getSortMode(): Flow<String>
}