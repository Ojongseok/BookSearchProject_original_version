package com.example.practice2.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.practice2.data.api.RetrofitInstance.api
import com.example.practice2.data.db.BookSearchDatabase
import com.example.practice2.data.model.Book
import com.example.practice2.data.model.SearchResponse
import com.example.practice2.data.repository.BookSearchRepositoryImpl.PreferencesKeys.SORT_MODE
import com.example.practice2.util.Constants.PAGING_SIZE
import com.example.practice2.util.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import retrofit2.Response
import java.io.IOException
import kotlin.system.exitProcess

class BookSearchRepositoryImpl(
    private val db: BookSearchDatabase,
    private val dataStore: DataStore<Preferences>
) : BookSearchRepository {
    override suspend fun searchBooks(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): Response<SearchResponse> {
        return api.searchBooks(query, sort, page, size)
    }

    override suspend fun insertBooks(book: Book) {
        db.bookSearchDao().insertBook(book)
    }

    override suspend fun deleteBooks(book: Book) {
        db.bookSearchDao().deleteBook(book)
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return db.bookSearchDao().getFavoriteBooks()
    }

    private object  PreferencesKeys {
        val SORT_MODE = stringPreferencesKey("sort_mode")
    }
    override suspend fun saveSortMode(mode: String) {
        dataStore.edit { prefs ->
            prefs[SORT_MODE] = mode
        }
    }

    override suspend fun getSortMode(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs[SORT_MODE] ?: Sort.ACCURACY.value
            }
    }

    override fun getFavoritePagingBooks(): Flow<PagingData<Book>> {
        val pagingSourceFactory = { db.bookSearchDao().getFavoritePagingBooks() }
        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE,
                enablePlaceholders = false,
                maxSize = PAGING_SIZE * 3
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow

    }

    override fun searchBooksPaging(query: String, sort: String): Flow<PagingData<Book>> {
        val pagingSourceFactory = { BookSearchPagingSource(query,sort) }

        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE,
                enablePlaceholders = false,
                maxSize = PAGING_SIZE*3
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}