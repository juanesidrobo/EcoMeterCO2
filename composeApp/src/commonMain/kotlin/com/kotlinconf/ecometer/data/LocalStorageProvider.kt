package com.kotlinconf.ecometer.data

import com.kotlinconf.ecometer.domain.ActivityEntry
import com.kotlinconf.ecometer.domain.TinyActionRecord
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Data class for export/import of all app data
 */
@Serializable
data class ExportPayload(
    val schemaVersion: Int = 1,
    val entries: List<ActivityEntry> = emptyList(),
    val tinyActionHistory: List<TinyActionRecord> = emptyList(),
    val exportedAt: String = ""
)

/**
 * Interface for local storage operations - platform specific implementations
 */
interface LocalStorageProvider {
    suspend fun saveEntries(entries: List<ActivityEntry>)
    suspend fun loadEntries(): List<ActivityEntry>
    suspend fun saveTinyActionHistory(records: List<TinyActionRecord>)
    suspend fun loadTinyActionHistory(): List<TinyActionRecord>
    suspend fun exportAllData(): String
    suspend fun importAllData(jsonData: String): Boolean
    suspend fun clearAll()
}

/**
 * JSON configuration for serialization
 */
val AppJson = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

/**
 * Common implementation helper for storage providers
 */
abstract class BaseLocalStorageProvider : LocalStorageProvider {

    override suspend fun exportAllData(): String {
        val entries = loadEntries()
        val tinyActionHistory = loadTinyActionHistory()
        val payload = ExportPayload(
            schemaVersion = 1,
            entries = entries,
            tinyActionHistory = tinyActionHistory,
            exportedAt = kotlinx.datetime.Clock.System.now().toString()
        )
        return AppJson.encodeToString(payload)
    }

    override suspend fun importAllData(jsonData: String): Boolean {
        return try {
            val payload = AppJson.decodeFromString<ExportPayload>(jsonData)
            // Replace all data (as per requirements)
            clearAll()
            saveEntries(payload.entries)
            saveTinyActionHistory(payload.tinyActionHistory)
            true
        } catch (e: Exception) {
            println("Import error: ${e.message}")
            false
        }
    }
}

