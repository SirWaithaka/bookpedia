package com.example.booktot.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object BookGraph : Route
    @Serializable
    data object BookList : Route

    @Serializable
    data class BookDetails(val id: String): Route
}