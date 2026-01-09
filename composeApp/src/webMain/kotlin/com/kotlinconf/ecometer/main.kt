package com.kotlinconf.ecometer

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.kotlinconf.ecometer.data.WebLocalStorageProvider
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    println("EcoMeterCO2: Starting application...")
    val storageProvider = WebLocalStorageProvider()
    println("EcoMeterCO2: Storage provider created")

    ComposeViewport(document.body!!) {
        App(storageProvider)
    }
    println("EcoMeterCO2: App initialized")
}