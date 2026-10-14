package com.example.booktot.book.presentation.book_list

import com.example.booktot.book.domain.Book
import com.example.booktot.core.presentation.UiText

data class BookListState(
    val searchQuery: String = "Kotlin",
    val searchResults: List<Book> = emptyList(),
    val favoriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
    val errorMessage: UiText? = null
)