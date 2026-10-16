package com.example.booktot

import androidx.compose.runtime.*
import com.example.booktot.book.data.network.KtorRemoteBookDataSource
import com.example.booktot.book.data.repository.BookRepository

import com.example.booktot.book.presentation.book_list.BookListScreenRoot
import com.example.booktot.book.presentation.book_list.BookListViewModel
import com.example.booktot.core.data.HttpClientFactory
import io.ktor.client.engine.HttpClientEngine

@Composable
fun App(engine: HttpClientEngine) {
    BookListScreenRoot(
        viewModel = remember {
            BookListViewModel(
                bookRepository = BookRepository(
                    KtorRemoteBookDataSource(
                        HttpClientFactory.create(engine)
                    )
                )
            )
        },
        onBookClick = {},
    )
}
