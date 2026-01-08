package com.kotlinconf.ecometer

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.kotlinconf.ecometer.data.LocalStorageProvider
import com.kotlinconf.ecometer.ui.HomeScreen

@Composable
fun App(storageProvider: LocalStorageProvider) {
    MaterialTheme {
        HomeScreen(storageProvider)
    }
}

// Preview without persistence
@Composable
@Preview
fun AppPreview() {
    MaterialTheme {
        // Preview uses in-memory only
    }
}
