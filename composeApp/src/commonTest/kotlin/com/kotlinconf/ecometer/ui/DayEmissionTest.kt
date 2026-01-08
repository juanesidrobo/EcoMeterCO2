package com.kotlinconf.ecometer.ui

import com.kotlinconf.ecometer.domain.Category
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit tests for DayEmission data class
 */
class DayEmissionTest {

    @Test
    fun dayEmissionHasAllProperties() {
        val emissionsByCategory = mapOf(
            Category.TRANSPORT to 5.0,
            Category.FOOD to 3.0
        )

        val dayEmission = DayEmission(
            dayLabel = "Mon",
            totalEmissions = 8.0,
            emissionsByCategory = emissionsByCategory
        )

        assertEquals("Mon", dayEmission.dayLabel)
        assertEquals(8.0, dayEmission.totalEmissions, 0.001)
        assertEquals(5.0, dayEmission.emissionsByCategory[Category.TRANSPORT] ?: 0.0, 0.001)
        assertEquals(3.0, dayEmission.emissionsByCategory[Category.FOOD] ?: 0.0, 0.001)
    }

    @Test
    fun dayEmissionCanHaveEmptyCategories() {
        val dayEmission = DayEmission(
            dayLabel = "Tue",
            totalEmissions = 0.0,
            emissionsByCategory = emptyMap()
        )

        assertEquals("Tue", dayEmission.dayLabel)
        assertEquals(0.0, dayEmission.totalEmissions, 0.001)
        assertTrue(dayEmission.emissionsByCategory.isEmpty())
    }

    @Test
    fun dayEmissionCanHaveAllCategories() {
        val emissionsByCategory = mapOf(
            Category.TRANSPORT to 2.0,
            Category.FOOD to 3.0,
            Category.ENERGY to 1.5,
            Category.PURCHASES to 5.0
        )

        val dayEmission = DayEmission(
            dayLabel = "Wed",
            totalEmissions = 11.5,
            emissionsByCategory = emissionsByCategory
        )

        assertEquals(4, dayEmission.emissionsByCategory.size)
    }

    @Test
    fun dayLabelsAreValid() {
        val validLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        validLabels.forEach { label ->
            val dayEmission = DayEmission(
                dayLabel = label,
                totalEmissions = 0.0,
                emissionsByCategory = emptyMap()
            )

            assertEquals(label, dayEmission.dayLabel)
        }
    }
}

