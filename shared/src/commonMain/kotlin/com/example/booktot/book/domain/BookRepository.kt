package com.example.booktot.book.domain

import com.example.booktot.core.domain.DataError
import com.example.booktot.core.domain.Result

interface BookRepository {
    suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote>
}
