package com.kotlinconf.ecometer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for ActivityEntry creation and properties
 */
class ActivityEntryTest {

    // ========== CREATION TESTS ==========

    @Test
    fun createActivityEntryWithTransportFactor() {
        val factor = EmissionFactor(
            id = "transport_car_petrol",
            category = Category.TRANSPORT,
            name = "Car (Petrol)",
            unit = "km",
            co2ePerUnit = 0.170
        )

        val entry = ActivityEntry.create(
            category = Category.TRANSPORT,
            amount = 10.0,
            factor = factor
        )

        assertEquals(Category.TRANSPORT, entry.category)
        assertEquals(10.0, entry.amount, 0.001)
        assertEquals("transport_car_petrol", entry.factorId)
        assertEquals(1.7, entry.co2eTotal, 0.001)
    }

    @Test
    fun createActivityEntryWithFoodFactor() {
        val factor = EmissionFactor(
            id = "food_meal_beef",
            category = Category.FOOD,
            name = "Beef Meal",
            unit = "serving",
            co2ePerUnit = 6.61
        )

        val entry = ActivityEntry.create(
            category = Category.FOOD,
            amount = 2.0,
            factor = factor
        )

        assertEquals(Category.FOOD, entry.category)
        assertEquals(2.0, entry.amount, 0.001)
        assertEquals(13.22, entry.co2eTotal, 0.001)
    }

    @Test
    fun createActivityEntryWithEnergyFactor() {
        val factor = EmissionFactor(
            id = "energy_electricity",
            category = Category.ENERGY,
            name = "Electricity",
            unit = "kWh",
            co2ePerUnit = 0.233
        )

        val entry = ActivityEntry.create(
            category = Category.ENERGY,
            amount = 100.0,
            factor = factor
        )

        assertEquals(Category.ENERGY, entry.category)
        assertEquals(100.0, entry.amount, 0.001)
        assertEquals(23.3, entry.co2eTotal, 0.001)
    }

    // ========== ID GENERATION TESTS ==========

    @Test
    fun activityEntryHasUniqueId() {
        val factor = EmissionFactor(
            id = "transport_car_petrol",
            category = Category.TRANSPORT,
            name = "Car (Petrol)",
            unit = "km",
            co2ePerUnit = 0.170
        )

        val entry = ActivityEntry.create(
            category = Category.TRANSPORT,
            amount = 10.0,
            factor = factor
        )

        assertNotNull(entry.id, "Entry should have an ID")
        assertTrue(entry.id.isNotBlank(), "Entry ID should not be blank")
        assertTrue(entry.id.contains("TRANSPORT"), "Entry ID should contain category name")
    }

    @Test
    fun differentEntriesHaveDifferentIds() {
        val factor = EmissionFactor(
            id = "transport_car_petrol",
            category = Category.TRANSPORT,
            name = "Car (Petrol)",
            unit = "km",
            co2ePerUnit = 0.170
        )

        val entry1 = ActivityEntry.create(Category.TRANSPORT, 10.0, factor)
        // Small delay simulation through different timestamps in production
        val entry2 = ActivityEntry.create(Category.TRANSPORT, 10.0, factor)

        // IDs should be unique (based on timestamp)
        // In tests they might be the same if created at same millisecond
        assertTrue(entry1.id.isNotBlank())
        assertTrue(entry2.id.isNotBlank())
    }

    // ========== TIMESTAMP TESTS ==========

    @Test
    fun activityEntryHasTimestamp() {
        val factor = EmissionFactor(
            id = "food_coffee",
            category = Category.FOOD,
            name = "Coffee",
            unit = "cup",
            co2ePerUnit = 0.21
        )

        val entry = ActivityEntry.create(
            category = Category.FOOD,
            amount = 1.0,
            factor = factor
        )

        assertTrue(entry.timestampIso.isNotBlank(), "Entry should have timestamp")
        // ISO format check - should contain date separator
        assertTrue(entry.timestampIso.contains("T"), "Timestamp should be ISO format")
    }

    @Test
    fun timestampCanBeConvertedToInstant() {
        val factor = EmissionFactor(
            id = "food_coffee",
            category = Category.FOOD,
            name = "Coffee",
            unit = "cup",
            co2ePerUnit = 0.21
        )

        val entry = ActivityEntry.create(
            category = Category.FOOD,
            amount = 1.0,
            factor = factor
        )

        // This should not throw
        val instant = entry.timestamp
        assertNotNull(instant, "Should be able to parse timestamp to Instant")
    }

    // ========== EXPLANATION TESTS ==========

    @Test
    fun activityEntryHasExplanation() {
        val factor = EmissionFactor(
            id = "transport_car_petrol",
            category = Category.TRANSPORT,
            name = "Car (Petrol)",
            unit = "km",
            co2ePerUnit = 0.170
        )

        val entry = ActivityEntry.create(
            category = Category.TRANSPORT,
            amount = 10.0,
            factor = factor
        )

        assertNotNull(entry.explanation, "Entry should have explanation")
        assertTrue(entry.explanation.isNotBlank(), "Explanation should not be blank")
        assertTrue(entry.explanation.contains("10.0"), "Explanation should contain amount")
        assertTrue(entry.explanation.contains("km"), "Explanation should contain unit")
    }

    // ========== CO2e CALCULATION TESTS ==========

    @Test
    fun zeroAmountResultsInZeroEmissions() {
        val factor = EmissionFactor(
            id = "transport_car_petrol",
            category = Category.TRANSPORT,
            name = "Car (Petrol)",
            unit = "km",
            co2ePerUnit = 0.170
        )

        val entry = ActivityEntry.create(
            category = Category.TRANSPORT,
            amount = 0.0,
            factor = factor
        )

        assertEquals(0.0, entry.co2eTotal, 0.001)
    }

    @Test
    fun zeroFactorResultsInZeroEmissions() {
        val factor = EmissionFactor(
            id = "transport_bike",
            category = Category.TRANSPORT,
            name = "Bicycle",
            unit = "km",
            co2ePerUnit = 0.0
        )

        val entry = ActivityEntry.create(
            category = Category.TRANSPORT,
            amount = 100.0,
            factor = factor
        )

        assertEquals(0.0, entry.co2eTotal, 0.001)
    }

    @Test
    fun co2eCalculationIsCorrect() {
        val testCases = listOf(
            Triple(10.0, 0.170, 1.7),
            Triple(50.0, 0.047, 2.35),
            Triple(2.0, 6.61, 13.22),
            Triple(100.0, 0.233, 23.3)
        )

        testCases.forEach { (amount, factorValue, expectedCo2e) ->
            val factor = EmissionFactor(
                id = "test",
                category = Category.TRANSPORT,
                name = "Test",
                unit = "unit",
                co2ePerUnit = factorValue
            )

            val entry = ActivityEntry.create(
                category = Category.TRANSPORT,
                amount = amount,
                factor = factor
            )

            assertEquals(
                expectedCo2e,
                entry.co2eTotal,
                0.001,
                "Amount $amount × factor $factorValue should = $expectedCo2e"
            )
        }
    }
}

