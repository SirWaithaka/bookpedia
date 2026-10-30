package com.example.booktot.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.booktot.book.data.database.DatabaseFactory
import com.example.booktot.book.data.database.FavoriteBookDatabase
import com.example.booktot.book.data.network.KtorRemoteBookDataSource
import com.example.booktot.book.data.network.RemoteBookDataSource
import com.example.booktot.book.data.repository.BookRepository
import com.example.booktot.book.presentation.SelectedBookViewModel
import com.example.booktot.book.presentation.book_detail.BookDetailViewModel
import com.example.booktot.book.presentation.book_list.BookListViewModel
import com.example.booktot.core.data.HttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()
    singleOf(::BookRepository).bind<com.example.booktot.book.domain.BookRepository>()

    single<FavoriteBookDatabase> {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<FavoriteBookDatabase>().favoriteBookDao }

    viewModelOf(::BookListViewModel)
    viewModelOf(::SelectedBookViewModel)
    viewModelOf(::BookDetailViewModel)
}
