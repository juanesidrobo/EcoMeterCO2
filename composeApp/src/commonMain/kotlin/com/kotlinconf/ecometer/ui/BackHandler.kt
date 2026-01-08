package com.kotlinconf.ecometer.ui

import androidx.compose.runtime.Composable

/**
 * Multiplatform BackHandler - handles back button/gesture navigation
 */
@Composable
expect fun BackHandler(enabled: Boolean = true, onBack: () -> Unit)

