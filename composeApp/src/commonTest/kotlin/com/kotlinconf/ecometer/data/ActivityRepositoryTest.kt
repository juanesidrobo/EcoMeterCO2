package com.kotlinconf.ecometer.data

import com.kotlinconf.ecometer.domain.ActivityEntry
import com.kotlinconf.ecometer.domain.Category
import com.kotlinconf.ecometer.domain.EmissionFactor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit tests for ActivityRepository
 */
class ActivityRepositoryTest {

    private fun createTestFactor(category: Category, co2ePerUnit: Double = 0.17): EmissionFactor {
        return EmissionFactor(
            id = "test_${category.name.lowercase()}",
            category = category,
            name = "Test ${category.name}",
            unit = "unit",
            co2ePerUnit = co2ePerUnit
        )
    }

    private fun createTestEntry(
        category: Category,
        amount: Double = 10.0,
        co2ePerUnit: Double = 0.17
    ): ActivityEntry {
        return ActivityEntry.create(
            category = category,
            amount = amount,
            factor = createTestFactor(category, co2ePerUnit)
        )
    }

    // ========== BASIC OPERATIONS TESTS ==========

    @Test
    fun addEntryStoresEntry() {
        val repository = ActivityRepository(null)

        val entry = createTestEntry(Category.TRANSPORT)
        repository.addEntry(entry)

        val entries = repository.getAllEntries()
        assertEquals(1, entries.size)
        assertEquals(entry.id, entries[0].id)
    }

    @Test
    fun addMultipleEntriesStoresAll() {
        val repository = ActivityRepository(null)

        repository.addEntry(createTestEntry(Category.TRANSPORT))
        repository.addEntry(createTestEntry(Category.FOOD))
        repository.addEntry(createTestEntry(Category.ENERGY))

        assertEquals(3, repository.getAllEntries().size)
    }

    @Test
    fun getAllEntriesReturnsAllEntries() {
        val repository = ActivityRepository(null)

        val entries = listOf(
            createTestEntry(Category.TRANSPORT),
            createTestEntry(Category.FOOD),
            createTestEntry(Category.ENERGY),
            createTestEntry(Category.PURCHASES)
        )

        entries.forEach { repository.addEntry(it) }

        assertEquals(4, repository.getAllEntries().size)
    }

    @Test
    fun getAllEntriesReturnsEmptyListWhenNoEntries() {
        val repository = ActivityRepository(null)

        assertTrue(repository.getAllEntries().isEmpty())
    }

    // ========== CATEGORY FILTER TESTS ==========

    @Test
    fun getEntriesByCategoryFiltersCorrectly() {
        val repository = ActivityRepository(null)

        repository.addEntry(createTestEntry(Category.TRANSPORT))
        repository.addEntry(createTestEntry(Category.TRANSPORT))
        repository.addEntry(createTestEntry(Category.FOOD))
        repository.addEntry(createTestEntry(Category.ENERGY))

        val transportEntries = repository.getEntriesByCategory(Category.TRANSPORT)
        assertEquals(2, transportEntries.size)
        transportEntries.forEach {
            assertEquals(Category.TRANSPORT, it.category)
        }
    }

    @Test
    fun getEntriesByCategoryReturnsEmptyWhenNoneMatch() {
        val repository = ActivityRepository(null)

        repository.addEntry(createTestEntry(Category.TRANSPORT))
        repository.addEntry(createTestEntry(Category.FOOD))

        val purchasesEntries = repository.getEntriesByCategory(Category.PURCHASES)
        assertTrue(purchasesEntries.isEmpty())
    }

    // ========== EMISSIONS CALCULATION TESTS ==========

    @Test
    fun getTodayEmissionsCalculatesCorrectly() {
        val repository = ActivityRepository(null)

        // Add entries with known emissions
        val factor = createTestFactor(Category.TRANSPORT, 0.5)
        val entry1 = ActivityEntry.create(Category.TRANSPORT, 10.0, factor) // 5.0 kg
        val entry2 = ActivityEntry.create(Category.TRANSPORT, 20.0, factor) // 10.0 kg

        repository.addEntry(entry1)
        repository.addEntry(entry2)

        val todayEmissions = repository.getTodayEmissions()
        assertEquals(15.0, todayEmissions, 0.001)
    }

    @Test
    fun getTodayEmissionsReturnsZeroWhenEmpty() {
        val repository = ActivityRepository(null)

        assertEquals(0.0, repository.getTodayEmissions(), 0.001)
    }

    @Test
    fun getEmissionsByCategoryForLastDaysGroupsCorrectly() {
        val repository = ActivityRepository(null)

        // Add entries from different categories
        val transportFactor = createTestFactor(Category.TRANSPORT, 0.5)
        val foodFactor = createTestFactor(Category.FOOD, 2.0)

        repository.addEntry(ActivityEntry.create(Category.TRANSPORT, 10.0, transportFactor)) // 5.0 kg
        repository.addEntry(ActivityEntry.create(Category.TRANSPORT, 10.0, transportFactor)) // 5.0 kg
        repository.addEntry(ActivityEntry.create(Category.FOOD, 2.0, foodFactor)) // 4.0 kg

        val emissionsByCategory = repository.getEmissionsByCategoryForLastDays(1)

        assertEquals(10.0, emissionsByCategory[Category.TRANSPORT] ?: 0.0, 0.001)
        assertEquals(4.0, emissionsByCategory[Category.FOOD] ?: 0.0, 0.001)
    }

    // ========== DELETE TESTS ==========

    @Test
    fun deleteEntryRemovesEntry() {
        val repository = ActivityRepository(null)

        val entry1 = createTestEntry(Category.TRANSPORT)
        val entry2 = createTestEntry(Category.FOOD)

        repository.addEntry(entry1)
        repository.addEntry(entry2)

        assertEquals(2, repository.getAllEntries().size)

        repository.deleteEntry(entry1.id)

        assertEquals(1, repository.getAllEntries().size)
        assertEquals(entry2.id, repository.getAllEntries()[0].id)
    }

    @Test
    fun deleteEntryDoesNothingForUnknownId() {
        val repository = ActivityRepository(null)

        val entry = createTestEntry(Category.TRANSPORT)
        repository.addEntry(entry)

        repository.deleteEntry("unknown_id")

        assertEquals(1, repository.getAllEntries().size)
    }

    // ========== CLEAR ALL TESTS ==========

    @Test
    fun clearAllRemovesAllEntries() {
        val repository = ActivityRepository(null)

        repository.addEntry(createTestEntry(Category.TRANSPORT))
        repository.addEntry(createTestEntry(Category.FOOD))
        repository.addEntry(createTestEntry(Category.ENERGY))

        assertEquals(3, repository.getAllEntries().size)

        repository.clearAll()

        assertTrue(repository.getAllEntries().isEmpty())
    }

    // ========== TODAY'S ENTRIES TESTS ==========

    @Test
    fun getTodayEntriesReturnsOnlyTodayEntries() {
        val repository = ActivityRepository(null)

        val entry = createTestEntry(Category.TRANSPORT)
        repository.addEntry(entry)

        val todayEntries = repository.getTodayEntries()

        assertEquals(1, todayEntries.size)
        assertEquals(entry.id, todayEntries[0].id)
    }
}

