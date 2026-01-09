package com.kotlinconf.ecometer.data

import com.kotlinconf.ecometer.domain.ActivityEntry
import com.kotlinconf.ecometer.domain.Category
import com.kotlinconf.ecometer.util.currentTimeMillis
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Repository for managing activity entries with persistence support.
 */
class ActivityRepository(
    private val storageProvider: LocalStorageProvider? = null
) {
    // In-memory storage
    private val entries = mutableListOf<ActivityEntry>()

    /**
     * Load entries from persistent storage
     */
    suspend fun loadFromStorage() {
        storageProvider?.let { storage ->
            val savedEntries = storage.loadEntries()
            entries.clear()
            entries.addAll(savedEntries)
            println("Loaded ${entries.size} entries from storage")
        }
    }

    /**
     * Save entries to persistent storage
     * Uses GlobalScope to ensure save completes even if caller scope is cancelled
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun saveToStorage() {
        storageProvider?.let { storage ->
            // Create a copy of entries to avoid concurrency issues
            val entriesToSave = entries.toList()
            GlobalScope.launch {
                try {
                    storage.saveEntries(entriesToSave)
                    println("Saved ${entriesToSave.size} entries to storage")
                } catch (e: Exception) {
                    println("Error saving entries: ${e.message}")
                }
            }
        }
    }

    fun addEntry(entry: ActivityEntry) {
        entries.add(entry)
        println("Added entry: ${entry.factorId}, total entries: ${entries.size}")
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
        val nowMillis = currentTimeMillis()
        val millisPerDay = 24L * 60 * 60 * 1000
        val cutoffMillis = nowMillis - (days * millisPerDay)

        return entries.filter { entry ->
            entry.timestampMillis >= cutoffMillis
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

