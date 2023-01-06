package com.example.practice2.data.repository

import com.example.practice2.data.model.SearchResponse
import retrofit2.Response

interface BookSearchRepository {
    suspend fun searchBooks(
        query : String,
        sort : String,
        page : Int,
        size : Int
    ) : Response<SearchResponse>
}
