package com.kotlinconf.ecometer.util

/**
 * Cross-platform time utilities.
 * Uses expect/actual to provide platform-specific implementations
 * that work correctly on all platforms including Wasm.
 */

/**
 * Get current time as epoch milliseconds
 */
expect fun currentTimeMillis(): Long

/**
 * Get current time as ISO-8601 string
 */
expect fun currentTimeIso(): String

/**
 * Parse ISO-8601 string to epoch milliseconds
 */
expect fun parseIsoToMillis(isoString: String): Long

/**
 * Convert epoch milliseconds to ISO-8601 string
 */
expect fun millisToIso(millis: Long): String

