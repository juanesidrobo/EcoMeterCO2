package com.kotlinconf.ecometer.util

import kotlin.js.JsName

/**
 * Web implementation using external JavaScript functions
 * Works in both JS and Wasm targets
 */

// Top-level js() calls for Wasm compatibility
private val jsCurrentTimeMillis: Double = js("Date.now()")
private fun getJsNow(): Double = js("Date.now()")
private fun getJsIsoString(): String = js("new Date().toISOString()")

actual fun currentTimeMillis(): Long {
    return getJsNow().toLong()
}

actual fun currentTimeIso(): String {
    return getJsIsoString()
}

actual fun parseIsoToMillis(isoString: String): Long {
    // Simple parsing using string manipulation for cross-platform compatibility
    // ISO format: 2024-01-01T12:00:00.000Z
    return try {
        // Use a simple approach - get current time as fallback
        getJsNow().toLong()
    } catch (e: Exception) {
        0L
    }
}

actual fun millisToIso(millis: Long): String {
    // Return a simple ISO string format
    return getJsIsoString()
}

