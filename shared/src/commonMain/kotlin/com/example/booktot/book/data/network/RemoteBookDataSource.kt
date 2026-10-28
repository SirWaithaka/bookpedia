package com.example.booktot.book.data.network

import com.example.booktot.book.data.dto.BookWorkDTO
import com.example.booktot.book.data.dto.SearchResponseDTO
import com.example.booktot.core.domain.DataError
import com.example.booktot.core.domain.Result

interface RemoteBookDataSource {
    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null,
    ): Result<SearchResponseDTO, DataError.Remote>

    suspend fun getBookDetails(bookId: String): Result<BookWorkDTO, DataError.Remote>
}
