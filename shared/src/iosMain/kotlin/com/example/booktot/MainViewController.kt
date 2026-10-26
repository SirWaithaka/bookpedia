package com.example.booktot

import androidx.compose.ui.window.ComposeUIViewController
import com.example.booktot.app.App
import com.example.booktot.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        try {
            initKoin()
        } catch (_: Throwable) {}
    }
) {
    App()
}
