package com.example.booktot.book.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDTO(
    @SerialName("docs") val results: List<SearchBookDTO>,
)
