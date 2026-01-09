package com.kotlinconf.ecometer.data

import com.kotlinconf.ecometer.domain.ActivityEntry
import com.kotlinconf.ecometer.domain.TinyActionRecord
import kotlinx.browser.localStorage
import kotlinx.serialization.encodeToString
import org.w3c.dom.get
import org.w3c.dom.set

/**
 * Web implementation using localStorage
 */
class WebLocalStorageProvider : BaseLocalStorageProvider() {

    override suspend fun saveEntries(entries: List<ActivityEntry>) {
        try {
            val json = AppJson.encodeToString(entries)
            localStorage[KEY_ENTRIES] = json
            println("WebStorage: Saved ${entries.size} entries, JSON length: ${json.length}")
        } catch (e: Exception) {
            println("WebStorage Error saving entries: ${e.message}")
            e.printStackTrace()
        }
    }

    override suspend fun loadEntries(): List<ActivityEntry> {
        return try {
            val json = localStorage[KEY_ENTRIES]
            println("WebStorage: Loading entries, JSON exists: ${json != null}")
            if (json != null && json.isNotEmpty()) {
                val entries = AppJson.decodeFromString<List<ActivityEntry>>(json)
                println("WebStorage: Loaded ${entries.size} entries")
                entries
            } else {
                println("WebStorage: No entries found")
                emptyList()
            }
        } catch (e: Exception) {
            println("WebStorage Error loading entries: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun saveTinyActionHistory(records: List<TinyActionRecord>) {
        try {
            val json = AppJson.encodeToString(records)
            localStorage[KEY_TINY_ACTIONS] = json
            println("WebStorage: Saved ${records.size} tiny action records")
        } catch (e: Exception) {
            println("WebStorage Error saving tiny action history: ${e.message}")
            e.printStackTrace()
        }
    }

    override suspend fun loadTinyActionHistory(): List<TinyActionRecord> {
        return try {
            val json = localStorage[KEY_TINY_ACTIONS]
            if (json != null && json.isNotEmpty()) {
                val records = AppJson.decodeFromString<List<TinyActionRecord>>(json)
                println("WebStorage: Loaded ${records.size} tiny action records")
                records
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("WebStorage Error loading tiny action history: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun clearAll() {
        localStorage.removeItem(KEY_ENTRIES)
        localStorage.removeItem(KEY_TINY_ACTIONS)
        println("WebStorage: Cleared all data")
    }

    companion object {
        private const val KEY_ENTRIES = "ecometer_activity_entries"
        private const val KEY_TINY_ACTIONS = "ecometer_tiny_action_history"
    }
}

