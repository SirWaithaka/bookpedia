package com.example.booktot.book.data.repository

import com.example.booktot.book.data.dto.toBook
import com.example.booktot.book.data.network.RemoteBookDataSource
import com.example.booktot.book.domain.Book
import com.example.booktot.book.domain.BookRepository
import com.example.booktot.core.domain.DataError
import com.example.booktot.core.domain.Result
import com.example.booktot.core.domain.map

class BookRepository(private val dataSource: RemoteBookDataSource) : BookRepository {

    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
        return dataSource.searchBooks(query).map { dto ->
            dto.results.map { it.toBook() }
        }
    }
}
