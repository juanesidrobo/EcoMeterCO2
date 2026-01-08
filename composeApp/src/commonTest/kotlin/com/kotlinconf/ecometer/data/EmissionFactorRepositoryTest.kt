package com.kotlinconf.ecometer.data

import com.kotlinconf.ecometer.domain.Category
import com.kotlinconf.ecometer.domain.EmissionFactor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for EmissionFactorRepository and HardcodedFactors
 */
class EmissionFactorRepositoryTest {

    // ========== HARDCODED FACTORS TESTS ==========

    @Test
    fun hardcodedFactorsContainsAllCategories() {
        val categories = Category.values().toSet()
        val factorCategories = HARDCODED_FACTORS.map { it.category }.toSet()

        categories.forEach { category ->
            assertTrue(
                factorCategories.contains(category),
                "Should have factors for category: $category"
            )
        }
    }

    @Test
    fun hardcodedFactorsHaveUniqueIds() {
        val ids = HARDCODED_FACTORS.map { it.id }
        val uniqueIds = ids.toSet()

        assertEquals(ids.size, uniqueIds.size, "All factor IDs should be unique")
    }

    @Test
    fun hardcodedFactorsHaveValidCo2eValues() {
        HARDCODED_FACTORS.forEach { factor ->
            assertTrue(
                factor.co2ePerUnit >= 0,
                "Factor ${factor.id} should have non-negative CO2e value"
            )
        }
    }

    @Test
    fun hardcodedFactorsHaveNames() {
        HARDCODED_FACTORS.forEach { factor ->
            assertTrue(
                factor.name.isNotBlank(),
                "Factor ${factor.id} should have a non-blank name"
            )
        }
    }

    @Test
    fun hardcodedFactorsHaveUnits() {
        HARDCODED_FACTORS.forEach { factor ->
            assertTrue(
                factor.unit.isNotBlank(),
                "Factor ${factor.id} should have a non-blank unit"
            )
        }
    }

    @Test
    fun hardcodedFactorsHaveSources() {
        HARDCODED_FACTORS.forEach { factor ->
            assertTrue(
                factor.source.isNotBlank(),
                "Factor ${factor.id} should have a non-blank source"
            )
        }
    }

    // ========== TRANSPORT FACTORS TESTS ==========

    @Test
    fun hasCarFactors() {
        val carFactors = HARDCODED_FACTORS.filter {
            it.category == Category.TRANSPORT && it.id.contains("car")
        }

        assertTrue(carFactors.isNotEmpty(), "Should have car transport factors")
    }

    @Test
    fun hasPublicTransportFactors() {
        val publicFactors = HARDCODED_FACTORS.filter {
            it.category == Category.TRANSPORT &&
                    (it.id.contains("bus") || it.id.contains("train") || it.id.contains("metro"))
        }

        assertTrue(publicFactors.isNotEmpty(), "Should have public transport factors")
    }

    @Test
    fun hasZeroEmissionTransportFactors() {
        val zeroEmissionFactors = HARDCODED_FACTORS.filter {
            it.category == Category.TRANSPORT && it.co2ePerUnit == 0.0
        }

        assertTrue(zeroEmissionFactors.isNotEmpty(), "Should have zero-emission transport options (walk, bike)")
    }

    @Test
    fun hasFlightFactors() {
        val flightFactors = HARDCODED_FACTORS.filter {
            it.category == Category.TRANSPORT && it.id.contains("flight")
        }

        assertTrue(flightFactors.isNotEmpty(), "Should have flight factors")
    }

    // ========== FOOD FACTORS TESTS ==========

    @Test
    fun hasMealFactors() {
        val mealFactors = HARDCODED_FACTORS.filter {
            it.category == Category.FOOD && it.id.contains("meal")
        }

        assertTrue(mealFactors.isNotEmpty(), "Should have meal factors")
    }

    @Test
    fun hasBeefFactor() {
        val beefFactor = HARDCODED_FACTORS.find { it.id == "food_meal_beef" }

        assertNotNull(beefFactor, "Should have beef meal factor")
        assertEquals("serving", beefFactor.unit)
        assertTrue(beefFactor.co2ePerUnit > 5.0, "Beef should have high emissions")
    }

    @Test
    fun hasVeganFactor() {
        val veganFactor = HARDCODED_FACTORS.find { it.id == "food_meal_vegan" }

        assertNotNull(veganFactor, "Should have vegan meal factor")
        assertTrue(veganFactor.co2ePerUnit < 1.0, "Vegan meal should have low emissions")
    }

    @Test
    fun foodFactorsOrderedByEmissions() {
        val mealFactors = HARDCODED_FACTORS.filter {
            it.category == Category.FOOD && it.id.contains("meal")
        }

        val beef = mealFactors.find { it.id.contains("beef") }
        val chicken = mealFactors.find { it.id.contains("chicken") }
        val vegan = mealFactors.find { it.id.contains("vegan") }

        if (beef != null && chicken != null) {
            assertTrue(beef.co2ePerUnit > chicken.co2ePerUnit, "Beef should have higher emissions than chicken")
        }

        if (chicken != null && vegan != null) {
            assertTrue(chicken.co2ePerUnit > vegan.co2ePerUnit, "Chicken should have higher emissions than vegan")
        }
    }

    // ========== ENERGY FACTORS TESTS ==========

    @Test
    fun hasElectricityFactor() {
        val electricityFactor = HARDCODED_FACTORS.find {
            it.category == Category.ENERGY && it.id.contains("electricity")
        }

        assertNotNull(electricityFactor, "Should have electricity factor")
        assertEquals("kWh", electricityFactor.unit)
    }

    @Test
    fun hasNaturalGasFactor() {
        val gasFactor = HARDCODED_FACTORS.find {
            it.category == Category.ENERGY && it.id.contains("gas")
        }

        assertNotNull(gasFactor, "Should have natural gas factor")
    }

    // ========== PURCHASES FACTORS TESTS ==========

    @Test
    fun hasClothingFactors() {
        val clothingFactors = HARDCODED_FACTORS.filter {
            it.category == Category.PURCHASES && it.id.contains("clothing")
        }

        assertTrue(clothingFactors.isNotEmpty(), "Should have clothing factors")
    }

    @Test
    fun hasElectronicsFactors() {
        val electronicsFactors = HARDCODED_FACTORS.filter {
            it.category == Category.PURCHASES &&
                (it.id.contains("smartphone") || it.id.contains("laptop") || it.id.contains("tv"))
        }

        assertTrue(electronicsFactors.isNotEmpty(), "Should have electronics factors")
    }

    @Test
    fun purchasesFactorsHaveVariety() {
        val purchasesFactors = HARDCODED_FACTORS.filter {
            it.category == Category.PURCHASES
        }

        val hasClothing = purchasesFactors.any { it.id.contains("clothing") }
        val hasElectronics = purchasesFactors.any { it.id.contains("smartphone") || it.id.contains("laptop") }
        val hasOther = purchasesFactors.any { it.id.contains("book") || it.id.contains("furniture") }

        assertTrue(
            hasClothing && hasElectronics,
            "Purchases should have variety (clothing and electronics)"
        )
    }

    // ========== DATA QUALITY TESTS ==========

    @Test
    fun noNegativeEmissionFactors() {
        val negativeFactors = HARDCODED_FACTORS.filter { it.co2ePerUnit < 0 }

        assertTrue(
            negativeFactors.isEmpty(),
            "Should not have negative emission factors"
        )
    }

    @Test
    fun factorValuesAreReasonable() {
        HARDCODED_FACTORS.forEach { factor ->
            // Electronics can have higher values (laptops up to 300kg CO2e)
            val maxAllowed = if (factor.category == Category.PURCHASES) 500.0 else 100.0
            assertTrue(
                factor.co2ePerUnit <= maxAllowed,
                "Factor ${factor.id} has unusually high value: ${factor.co2ePerUnit}"
            )
        }
    }

    @Test
    fun minimumFactorCount() {
        assertTrue(
            HARDCODED_FACTORS.size >= 20,
            "Should have at least 20 emission factors"
        )
    }
}

