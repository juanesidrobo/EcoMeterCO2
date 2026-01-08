package com.kotlinconf.ecometer.data

import com.kotlinconf.ecometer.domain.ActivityEntry
import com.kotlinconf.ecometer.domain.TinyActionRecord
import kotlinx.browser.localStorage
import kotlinx.serialization.encodeToString

/**
 * Web implementation using localStorage
 */
class WebLocalStorageProvider : BaseLocalStorageProvider() {

    override suspend fun saveEntries(entries: List<ActivityEntry>) {
        try {
            val json = AppJson.encodeToString(entries)
            localStorage.setItem(KEY_ENTRIES, json)
        } catch (e: Exception) {
            println("Error saving entries: ${e.message}")
        }
    }

    override suspend fun loadEntries(): List<ActivityEntry> {
        return try {
            val json = localStorage.getItem(KEY_ENTRIES)
            if (json != null) {
                AppJson.decodeFromString<List<ActivityEntry>>(json)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error loading entries: ${e.message}")
            emptyList()
        }
    }

    override suspend fun saveTinyActionHistory(records: List<TinyActionRecord>) {
        try {
            val json = AppJson.encodeToString(records)
            localStorage.setItem(KEY_TINY_ACTIONS, json)
        } catch (e: Exception) {
            println("Error saving tiny action history: ${e.message}")
        }
    }

    override suspend fun loadTinyActionHistory(): List<TinyActionRecord> {
        return try {
            val json = localStorage.getItem(KEY_TINY_ACTIONS)
            if (json != null) {
                AppJson.decodeFromString<List<TinyActionRecord>>(json)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error loading tiny action history: ${e.message}")
            emptyList()
        }
    }

    override suspend fun clearAll() {
        localStorage.removeItem(KEY_ENTRIES)
        localStorage.removeItem(KEY_TINY_ACTIONS)
    }

    companion object {
        private const val KEY_ENTRIES = "ecometer_activity_entries"
        private const val KEY_TINY_ACTIONS = "ecometer_tiny_action_history"
    }
}

