package com.kotlinconf.ecometer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit tests for FootprintCalculator
 * Tests CO2e calculation for various activity types and edge cases
 */
class FootprintCalculatorTest {

    private val calculator = FootprintCalculator()

    // ========== TRANSPORT TESTS ==========

    @Test
    fun calculateCarPetrolEmissions() {
        val factor = EmissionFactor(
            id = "transport_car_petrol",
            category = Category.TRANSPORT,
            name = "Car (Petrol)",
            unit = "km",
            co2ePerUnit = 0.170
        )

        val result = calculator.calculate(10.0, factor)

        assertEquals(1.7, result, 0.001, "10 km × 0.170 should = 1.7 kgCO2e")
    }

    @Test
    fun calculateElectricCarEmissions() {
        val factor = EmissionFactor(
            id = "transport_car_electric",
            category = Category.TRANSPORT,
            name = "Electric Car",
            unit = "km",
            co2ePerUnit = 0.047
        )

        val result = calculator.calculate(50.0, factor)

        assertEquals(2.35, result, 0.001, "50 km × 0.047 should = 2.35 kgCO2e")
    }

    @Test
    fun calculateZeroEmissionsForBicycle() {
        val factor = EmissionFactor(
            id = "transport_bike",
            category = Category.TRANSPORT,
            name = "Bicycle",
            unit = "km",
            co2ePerUnit = 0.0
        )

        val result = calculator.calculate(100.0, factor)

        assertEquals(0.0, result, 0.001, "Bicycle should have zero emissions")
    }

    @Test
    fun calculateFlightEmissions() {
        val factor = EmissionFactor(
            id = "transport_flight_short",
            category = Category.TRANSPORT,
            name = "Short Flight (<3h)",
            unit = "km",
            co2ePerUnit = 0.255
        )

        val result = calculator.calculate(500.0, factor)

        assertEquals(127.5, result, 0.001, "500 km flight × 0.255 should = 127.5 kgCO2e")
    }

    // ========== FOOD TESTS ==========

    @Test
    fun calculateBeefMealEmissions() {
        val factor = EmissionFactor(
            id = "food_meal_beef",
            category = Category.FOOD,
            name = "Beef Meal",
            unit = "serving",
            co2ePerUnit = 6.61
        )

        val result = calculator.calculate(2.0, factor)

        assertEquals(13.22, result, 0.001, "2 beef meals × 6.61 should = 13.22 kgCO2e")
    }

    @Test
    fun calculateVeganMealEmissions() {
        val factor = EmissionFactor(
            id = "food_meal_vegan",
            category = Category.FOOD,
            name = "Vegan Meal",
            unit = "serving",
            co2ePerUnit = 0.35
        )

        val result = calculator.calculate(3.0, factor)

        assertEquals(1.05, result, 0.001, "3 vegan meals × 0.35 should = 1.05 kgCO2e")
    }

    @Test
    fun veganHasLessEmissionsThanBeef() {
        val beefFactor = EmissionFactor(
            id = "food_meal_beef",
            category = Category.FOOD,
            name = "Beef Meal",
            unit = "serving",
            co2ePerUnit = 6.61
        )
        val veganFactor = EmissionFactor(
            id = "food_meal_vegan",
            category = Category.FOOD,
            name = "Vegan Meal",
            unit = "serving",
            co2ePerUnit = 0.35
        )

        val beefEmissions = calculator.calculate(1.0, beefFactor)
        val veganEmissions = calculator.calculate(1.0, veganFactor)

        assertTrue(veganEmissions < beefEmissions, "Vegan meal should have less emissions than beef")
    }

    // ========== ENERGY TESTS ==========

    @Test
    fun calculateElectricityEmissions() {
        val factor = EmissionFactor(
            id = "energy_electricity",
            category = Category.ENERGY,
            name = "Electricity",
            unit = "kWh",
            co2ePerUnit = 0.233
        )

        val result = calculator.calculate(100.0, factor)

        assertEquals(23.3, result, 0.001, "100 kWh × 0.233 should = 23.3 kgCO2e")
    }

    @Test
    fun calculateNaturalGasEmissions() {
        val factor = EmissionFactor(
            id = "energy_natural_gas",
            category = Category.ENERGY,
            name = "Natural Gas",
            unit = "kWh",
            co2ePerUnit = 0.184
        )

        val result = calculator.calculate(50.0, factor)

        assertEquals(9.2, result, 0.001, "50 kWh gas × 0.184 should = 9.2 kgCO2e")
    }

    // ========== PURCHASES TESTS ==========

    @Test
    fun calculateClothingEmissions() {
        val factor = EmissionFactor(
            id = "purchase_clothing_small",
            category = Category.PURCHASES,
            name = "Clothing (Small)",
            unit = "item",
            co2ePerUnit = 5.0
        )

        val result = calculator.calculate(3.0, factor)

        assertEquals(15.0, result, 0.001, "3 clothing items × 5.0 should = 15.0 kgCO2e")
    }

    @Test
    fun calculateElectronicsEmissions() {
        val factor = EmissionFactor(
            id = "purchase_electronics_medium",
            category = Category.PURCHASES,
            name = "Electronics (Medium)",
            unit = "item",
            co2ePerUnit = 70.0
        )

        val result = calculator.calculate(1.0, factor)

        assertEquals(70.0, result, 0.001, "1 electronics item × 70.0 should = 70.0 kgCO2e")
    }

    // ========== EDGE CASES ==========

    @Test
    fun calculateZeroAmount() {
        val factor = EmissionFactor(
            id = "transport_car_petrol",
            category = Category.TRANSPORT,
            name = "Car (Petrol)",
            unit = "km",
            co2ePerUnit = 0.170
        )

        val result = calculator.calculate(0.0, factor)

        assertEquals(0.0, result, 0.001, "Zero amount should result in zero emissions")
    }

    @Test
    fun calculateSmallDecimalAmount() {
        val factor = EmissionFactor(
            id = "food_coffee",
            category = Category.FOOD,
            name = "Coffee",
            unit = "cup",
            co2ePerUnit = 0.21
        )

        val result = calculator.calculate(0.5, factor)

        assertEquals(0.105, result, 0.001, "0.5 cups × 0.21 should = 0.105 kgCO2e")
    }

    @Test
    fun calculateLargeAmount() {
        val factor = EmissionFactor(
            id = "energy_electricity",
            category = Category.ENERGY,
            name = "Electricity",
            unit = "kWh",
            co2ePerUnit = 0.233
        )

        val result = calculator.calculate(10000.0, factor)

        assertEquals(2330.0, result, 0.001, "10000 kWh × 0.233 should = 2330.0 kgCO2e")
    }

    @Test
    fun emissionsAreNonNegative() {
        val factor = EmissionFactor(
            id = "transport_car_petrol",
            category = Category.TRANSPORT,
            name = "Car (Petrol)",
            unit = "km",
            co2ePerUnit = 0.170
        )

        val amounts = listOf(0.0, 1.0, 10.0, 100.0, 0.001)

        amounts.forEach { amount ->
            val result = calculator.calculate(amount, factor)
            assertTrue(result >= 0.0, "Emissions should never be negative for amount $amount")
        }
    }

    @Test
    fun emissionsIncreaseWithAmount() {
        val factor = EmissionFactor(
            id = "transport_car_petrol",
            category = Category.TRANSPORT,
            name = "Car (Petrol)",
            unit = "km",
            co2ePerUnit = 0.170
        )

        val result10km = calculator.calculate(10.0, factor)
        val result20km = calculator.calculate(20.0, factor)
        val result50km = calculator.calculate(50.0, factor)

        assertTrue(result10km < result20km, "More km should mean more emissions")
        assertTrue(result20km < result50km, "More km should mean more emissions")
    }
}

