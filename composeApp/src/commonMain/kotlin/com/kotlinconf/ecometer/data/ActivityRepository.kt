package com.kotlinconf.ecometer.data

import com.kotlinconf.ecometer.domain.ActivityEntry
import com.kotlinconf.ecometer.domain.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus

/**
 * Repository for managing activity entries with persistence support.
 */
class ActivityRepository(
    private val storageProvider: LocalStorageProvider? = null
) {
    // In-memory storage
    private val entries = mutableListOf<ActivityEntry>()
    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * Load entries from persistent storage
     */
    suspend fun loadFromStorage() {
        storageProvider?.let { storage ->
            val savedEntries = storage.loadEntries()
            entries.clear()
            entries.addAll(savedEntries)
        }
    }

    /**
     * Save entries to persistent storage
     */
    private fun saveToStorage() {
        storageProvider?.let { storage ->
            scope.launch {
                storage.saveEntries(entries.toList())
            }
        }
    }

    fun addEntry(entry: ActivityEntry) {
        entries.add(entry)
        saveToStorage()
    }

    fun getAllEntries(): List<ActivityEntry> {
        return entries.toList()
    }

    fun getEntriesByCategory(category: Category): List<ActivityEntry> {
        return entries.filter { it.category == category }
    }

    /**
     * Get entries from the last N days
     */
    fun getEntriesFromLastDays(days: Int): List<ActivityEntry> {
        val now = Clock.System.now()
        val cutoff = now.minus(days, DateTimeUnit.DAY, TimeZone.currentSystemDefault())

        return entries.filter { entry ->
            entry.timestamp >= cutoff
        }
    }

    /**
     * Get total CO2e emissions for the last N days
     */
    fun getTotalEmissionsForLastDays(days: Int): Double {
        return getEntriesFromLastDays(days).sumOf { it.co2eTotal }
    }

    /**
     * Get emissions grouped by category for the last N days
     */
    fun getEmissionsByCategoryForLastDays(days: Int): Map<Category, Double> {
        return getEntriesFromLastDays(days)
            .groupBy { it.category }
            .mapValues { (_, entries) -> entries.sumOf { it.co2eTotal } }
    }

    /**
     * Get today's total emissions
     */
    fun getTodayEmissions(): Double {
        return getTotalEmissionsForLastDays(1)
    }

    /**
     * Get today's entries
     */
    fun getTodayEntries(): List<ActivityEntry> {
        return getEntriesFromLastDays(1)
    }

    /**
     * Delete an entry by ID
     */
    fun deleteEntry(entryId: String) {
        entries.removeAll { it.id == entryId }
        saveToStorage()
    }

    /**
     * Clear all entries
     */
    fun clearAll() {
        entries.clear()
        saveToStorage()
    }

    /**
     * Export all data as JSON string
     */
    suspend fun exportData(): String? {
        return storageProvider?.exportAllData()
    }

    /**
     * Import data from JSON string (replaces existing data)
     */
    suspend fun importData(jsonData: String): Boolean {
        val result = storageProvider?.importAllData(jsonData) ?: false
        if (result) {
            loadFromStorage()
        }
        return result
    }
}

