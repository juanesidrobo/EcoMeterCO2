package com.kotlinconf.ecometer.ui

import androidx.compose.runtime.Composable
import androidx.activity.compose.BackHandler as AndroidBackHandler

/**
 * Android implementation of BackHandler using Activity Compose
 */
@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    AndroidBackHandler(enabled = enabled, onBack = onBack)
}

