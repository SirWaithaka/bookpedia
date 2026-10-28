package com.example.booktot.book.presentation.book_detail

import com.example.booktot.book.domain.Book

sealed interface BookDetailAction {
    data object OnBackClick : BookDetailAction
    data object OnFavoriteClick : BookDetailAction
    data class OnSelectedBookChange(val book: Book) : BookDetailAction
}
