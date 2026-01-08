package com.kotlinconf.ecometer

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.kotlinconf.ecometer.data.WebLocalStorageProvider

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val storageProvider = WebLocalStorageProvider()
    ComposeViewport {
        App(storageProvider)
    }
}