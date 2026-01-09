package com.kotlinconf.ecometer.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * Android implementation using kotlinx-datetime
 */

actual fun currentTimeMillis(): Long {
    return Clock.System.now().toEpochMilliseconds()
}

actual fun currentTimeIso(): String {
    return Clock.System.now().toString()
}

actual fun parseIsoToMillis(isoString: String): Long {
    return Instant.parse(isoString).toEpochMilliseconds()
}

actual fun millisToIso(millis: Long): String {
    return Instant.fromEpochMilliseconds(millis).toString()
}

