package com.example.booktot.book.presentation.book_detail

import com.example.booktot.book.domain.Book

data class BookDetailState(
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false,
    val book: Book? = null
)
