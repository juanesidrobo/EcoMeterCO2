package com.kotlinconf.ecometer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for Category enum
 */
class CategoryTest {

    @Test
    fun hasFourCategories() {
        assertEquals(4, Category.values().size)
    }

    @Test
    fun hasTransportCategory() {
        val category = Category.TRANSPORT
        assertNotNull(category)
    }

    @Test
    fun hasFoodCategory() {
        val category = Category.FOOD
        assertNotNull(category)
    }

    @Test
    fun hasEnergyCategory() {
        val category = Category.ENERGY
        assertNotNull(category)
    }

    @Test
    fun hasPurchasesCategory() {
        val category = Category.PURCHASES
        assertNotNull(category)
    }

    @Test
    fun categoriesAreDistinct() {
        val categories = Category.values().toSet()
        assertEquals(4, categories.size)
    }

    @Test
    fun categoryNamesAreUppercase() {
        Category.values().forEach { category ->
            assertEquals(category.name, category.name.uppercase())
        }
    }
}

