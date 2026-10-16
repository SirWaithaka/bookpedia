package com.example.booktot.book.data.network

import com.example.booktot.book.data.dto.SearchResponseDTO
import com.example.booktot.core.data.safeCall
import com.example.booktot.core.domain.DataError
import com.example.booktot.core.domain.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

private const val BASE_URL = "https://openlibrary.org"

class KtorRemoteBookDataSource(
    private val httpClient: HttpClient
) : RemoteBookDataSource {

    override suspend fun searchBooks(
        query: String,
        resultLimit: Int?,
    ): Result<SearchResponseDTO, DataError.Remote> {
        return safeCall {
            httpClient.get(urlString = "$BASE_URL/search.json") {
                parameter("q", query)
                parameter("limit", resultLimit)
                parameter("language", "eng")
                parameter(
                    "fields",
                    "key,title,language,cover_i,author_key,author_name,cover_edition_key,first_publish_year,ratings_year,ratings_count,number_of_pages_median,edition_count"
                )
            }
        }
    }

}