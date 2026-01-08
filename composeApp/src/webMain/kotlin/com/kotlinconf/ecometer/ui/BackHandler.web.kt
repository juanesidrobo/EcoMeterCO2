package com.kotlinconf.ecometer.ui

import androidx.compose.runtime.Composable

/**
 * Web implementation of BackHandler - no-op on web as browser handles navigation
 */
@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // Web doesn't have a native back button handler in the same way Android does
    // The browser back button is handled separately
    // For now, this is a no-op on web
}

