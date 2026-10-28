package com.example.booktot.book.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = BookWorkDTOSerializer::class)
data class BookWorkDTO (
    @SerialName("description") val description: String? = null,
)

@Serializable
data class DescriptionDTO(
    @SerialName("value") val value: String,
)