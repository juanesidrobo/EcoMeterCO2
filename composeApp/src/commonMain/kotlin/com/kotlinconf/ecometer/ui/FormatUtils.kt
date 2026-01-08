package com.kotlinconf.ecometer.ui

import kotlin.math.pow
import kotlin.math.round

/**
 * Utility functions for formatting values in the UI.
 * These are multiplatform compatible (no JVM-only String.format).
 */

/**
 * Format a double to a string with specified decimal places.
 */
fun formatNumber(value: Double, decimals: Int = 2): String {
    if (decimals < 0) return value.toString()

    val factor = 10.0.pow(decimals)
    val rounded = round(value * factor) / factor

    // Convert to string and ensure proper decimal places
    val str = rounded.toString()
    val parts = str.split(".")

    return if (decimals == 0) {
        parts[0]
    } else if (parts.size == 1) {
        // No decimal point, add zeros
        "${parts[0]}.${"0".repeat(decimals)}"
    } else {
        // Has decimal point, pad or truncate
        val decimalPart = parts[1].take(decimals).padEnd(decimals, '0')
        "${parts[0]}.$decimalPart"
    }
}

/**
 * Format CO2e emissions value.
 */
fun formatEmissions(value: Double): String {
    return "${formatNumber(value, 2)} kg CO2e"
}

/**
 * Format percentage value.
 */
fun formatPercent(value: Double): String {
    return "${formatNumber(value, 0)}%"
}

