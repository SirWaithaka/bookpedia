package com.example.booktot.book.data.repository

import androidx.sqlite.SQLiteException
import com.example.booktot.book.data.database.FavoriteBookDao
import com.example.booktot.book.data.database.toBook
import com.example.booktot.book.data.database.toBookEntity
import com.example.booktot.book.data.dto.toBook
import com.example.booktot.book.data.network.RemoteBookDataSource
import com.example.booktot.book.domain.Book
import com.example.booktot.book.domain.BookRepository
import com.example.booktot.core.domain.DataError
import com.example.booktot.core.domain.EmptyResult
import com.example.booktot.core.domain.Result
import com.example.booktot.core.domain.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookRepository(
    private val dataSource: RemoteBookDataSource,
    private val favoriteBookDao: FavoriteBookDao,
    ) : BookRepository {

    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
        return dataSource.searchBooks(query).map { dto ->
            dto.results.map { it.toBook() }
        }
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        // check local db first for book, otherwise call remote api
        val localResult = favoriteBookDao.getFavoriteBook(bookId)

        return if (localResult == null) {
            dataSource.getBookDetails(bookId).map { it.description }
        } else {
            Result.Success(localResult.description)
        }
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return favoriteBookDao.getFavoriteBooks()
            .map { entities -> entities.map { it.toBook() } }
    }

    override fun isBookFavorite(id: String): Flow<Boolean> {
        return favoriteBookDao.getFavoriteBooks()
            .map { entities -> entities.any { it.id == id }}
    }

    override suspend fun markAsFavorite(book: Book): EmptyResult<DataError.Local> {
        return try {
            favoriteBookDao.upsert(book.toBookEntity())
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteFromFavorites(id: String) {
        favoriteBookDao.deleteFavoriteBook(id)
    }


}
