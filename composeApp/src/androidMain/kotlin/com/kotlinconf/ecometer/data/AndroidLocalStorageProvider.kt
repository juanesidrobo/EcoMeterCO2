package com.kotlinconf.ecometer.data

import android.content.Context
import android.content.SharedPreferences
import com.kotlinconf.ecometer.domain.ActivityEntry
import com.kotlinconf.ecometer.domain.TinyActionRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString

/**
 * Android implementation using SharedPreferences for simplicity
 * For production, consider using DataStore or Room
 */
class AndroidLocalStorageProvider(
    private val context: Context
) : BaseLocalStorageProvider() {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override suspend fun saveEntries(entries: List<ActivityEntry>) {
        withContext(Dispatchers.IO) {
            val json = AppJson.encodeToString(entries)
            prefs.edit().putString(KEY_ENTRIES, json).apply()
        }
    }

    override suspend fun loadEntries(): List<ActivityEntry> {
        return withContext(Dispatchers.IO) {
            val json = prefs.getString(KEY_ENTRIES, null)
            if (json != null) {
                try {
                    AppJson.decodeFromString<List<ActivityEntry>>(json)
                } catch (e: Exception) {
                    println("Error loading entries: ${e.message}")
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }

    override suspend fun saveTinyActionHistory(records: List<TinyActionRecord>) {
        withContext(Dispatchers.IO) {
            val json = AppJson.encodeToString(records)
            prefs.edit().putString(KEY_TINY_ACTIONS, json).apply()
        }
    }

    override suspend fun loadTinyActionHistory(): List<TinyActionRecord> {
        return withContext(Dispatchers.IO) {
            val json = prefs.getString(KEY_TINY_ACTIONS, null)
            if (json != null) {
                try {
                    AppJson.decodeFromString<List<TinyActionRecord>>(json)
                } catch (e: Exception) {
                    println("Error loading tiny action history: ${e.message}")
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }

    override suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            prefs.edit().clear().apply()
        }
    }

    companion object {
        private const val PREFS_NAME = "ecometer_data"
        private const val KEY_ENTRIES = "activity_entries"
        private const val KEY_TINY_ACTIONS = "tiny_action_history"
    }
}

