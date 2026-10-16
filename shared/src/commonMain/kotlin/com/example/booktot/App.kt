package com.example.booktot

import androidx.compose.runtime.*

import com.example.booktot.book.presentation.book_list.BookListScreenRoot
import com.example.booktot.book.presentation.book_list.BookListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val viewModel = koinViewModel<BookListViewModel>()

    BookListScreenRoot(
        viewModel = viewModel,
        onBookClick = {},
    )
}
