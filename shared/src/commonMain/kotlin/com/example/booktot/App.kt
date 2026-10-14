package com.example.booktot

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

import com.example.booktot.book.presentation.book_list.BookListScreenRoot
import com.example.booktot.book.presentation.book_list.BookListViewModel

@Composable
@Preview
fun App() {
    BookListScreenRoot(
        viewModel = remember { BookListViewModel () },
        onBookClick = {},
    )
}
