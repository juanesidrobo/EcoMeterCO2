package com.kotlinconf.ecometer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for EmissionFactor model
 */
class EmissionFactorTest {

    // ========== MODEL TESTS ==========

    @Test
    fun emissionFactorHasRequiredProperties() {
        val factor = EmissionFactor(
            id = "test_factor",
            category = Category.TRANSPORT,
            name = "Test Factor",
            unit = "km",
            co2ePerUnit = 0.5,
            source = "Test Source"
        )

        assertEquals("test_factor", factor.id)
        assertEquals(Category.TRANSPORT, factor.category)
        assertEquals("Test Factor", factor.name)
        assertEquals("km", factor.unit)
        assertEquals(0.5, factor.co2ePerUnit, 0.001)
        assertEquals("Test Source", factor.source)
    }

    @Test
    fun emissionFactorHasDefaultSource() {
        val factor = EmissionFactor(
            id = "test_factor",
            category = Category.TRANSPORT,
            name = "Test Factor",
            unit = "km",
            co2ePerUnit = 0.5
        )

        assertEquals("UK Gov GHG 2025", factor.source)
    }

    // ========== CATEGORY COVERAGE TESTS ==========

    @Test
    fun canCreateTransportFactor() {
        val factor = EmissionFactor(
            id = "transport_car",
            category = Category.TRANSPORT,
            name = "Car",
            unit = "km",
            co2ePerUnit = 0.170
        )

        assertEquals(Category.TRANSPORT, factor.category)
    }

    @Test
    fun canCreateFoodFactor() {
        val factor = EmissionFactor(
            id = "food_beef",
            category = Category.FOOD,
            name = "Beef Meal",
            unit = "serving",
            co2ePerUnit = 6.61
        )

        assertEquals(Category.FOOD, factor.category)
    }

    @Test
    fun canCreateEnergyFactor() {
        val factor = EmissionFactor(
            id = "energy_electricity",
            category = Category.ENERGY,
            name = "Electricity",
            unit = "kWh",
            co2ePerUnit = 0.233
        )

        assertEquals(Category.ENERGY, factor.category)
    }

    @Test
    fun canCreatePurchasesFactor() {
        val factor = EmissionFactor(
            id = "purchase_clothing",
            category = Category.PURCHASES,
            name = "Clothing",
            unit = "item",
            co2ePerUnit = 5.0
        )

        assertEquals(Category.PURCHASES, factor.category)
    }

    // ========== UNIT VARIETY TESTS ==========

    @Test
    fun supportsDistanceUnit() {
        val factor = EmissionFactor(
            id = "test",
            category = Category.TRANSPORT,
            name = "Test",
            unit = "km",
            co2ePerUnit = 0.1
        )

        assertEquals("km", factor.unit)
    }

    @Test
    fun supportsServingUnit() {
        val factor = EmissionFactor(
            id = "test",
            category = Category.FOOD,
            name = "Test",
            unit = "serving",
            co2ePerUnit = 1.0
        )

        assertEquals("serving", factor.unit)
    }

    @Test
    fun supportsKwhUnit() {
        val factor = EmissionFactor(
            id = "test",
            category = Category.ENERGY,
            name = "Test",
            unit = "kWh",
            co2ePerUnit = 0.2
        )

        assertEquals("kWh", factor.unit)
    }

    @Test
    fun supportsItemUnit() {
        val factor = EmissionFactor(
            id = "test",
            category = Category.PURCHASES,
            name = "Test",
            unit = "item",
            co2ePerUnit = 5.0
        )

        assertEquals("item", factor.unit)
    }

    // ========== CO2e VALUE TESTS ==========

    @Test
    fun co2ePerUnitCanBeZero() {
        val factor = EmissionFactor(
            id = "bicycle",
            category = Category.TRANSPORT,
            name = "Bicycle",
            unit = "km",
            co2ePerUnit = 0.0
        )

        assertEquals(0.0, factor.co2ePerUnit, 0.001)
    }

    @Test
    fun co2ePerUnitCanBeSmall() {
        val factor = EmissionFactor(
            id = "test",
            category = Category.TRANSPORT,
            name = "Test",
            unit = "km",
            co2ePerUnit = 0.001
        )

        assertTrue(factor.co2ePerUnit > 0 && factor.co2ePerUnit < 0.01)
    }

    @Test
    fun co2ePerUnitCanBeLarge() {
        val factor = EmissionFactor(
            id = "flight",
            category = Category.TRANSPORT,
            name = "Flight",
            unit = "km",
            co2ePerUnit = 0.255
        )

        assertTrue(factor.co2ePerUnit > 0.2)
    }
}

