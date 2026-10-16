package com.example.booktot.core.presentation

import booktot.shared.generated.resources.Res
import booktot.shared.generated.resources.error_no_internet
import booktot.shared.generated.resources.error_request_timeout
import booktot.shared.generated.resources.error_serialization
import booktot.shared.generated.resources.error_storage_full
import booktot.shared.generated.resources.error_too_many_requests
import booktot.shared.generated.resources.error_unknown
import com.example.booktot.core.domain.DataError

fun DataError.toUiText(): UiText {
    val stringRes = when(this) {
        DataError.Local.DISK_FULL -> Res.string.error_storage_full
        DataError.Local.UNKNOWN -> Res.string.error_unknown
        DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
        DataError.Remote.SERVER -> Res.string.error_unknown
        DataError.Remote.SERIALIZATION -> Res.string.error_serialization
        DataError.Remote.UNKNOWN -> Res.string.error_unknown
    }

    return UiText.StringResourceId(stringRes)
}
