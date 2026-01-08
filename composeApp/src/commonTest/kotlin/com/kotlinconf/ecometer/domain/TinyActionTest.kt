package com.kotlinconf.ecometer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for TinyAction catalog and related logic
 */
class TinyActionTest {

    // ========== CATALOG TESTS ==========

    @Test
    fun catalogContainsActionsForAllCategories() {
        val categories = Category.values()

        categories.forEach { category ->
            val actions = TinyActionCatalog.getActionsByCategory(category)
            assertTrue(
                actions.isNotEmpty(),
                "Should have at least one action for category: $category"
            )
        }
    }

    @Test
    fun catalogContainsExpectedNumberOfActions() {
        val allActions = TinyActionCatalog.actions

        assertTrue(allActions.size >= 12, "Should have at least 12 actions in catalog")
    }

    @Test
    fun allActionsHaveValidEffortScores() {
        TinyActionCatalog.actions.forEach { action ->
            assertTrue(
                action.effortScore in 1..5,
                "Action ${action.id} should have effort score between 1 and 5, got ${action.effortScore}"
            )
        }
    }

    @Test
    fun allActionsHavePositiveImpact() {
        TinyActionCatalog.actions.forEach { action ->
            assertTrue(
                action.impactEstimateCo2e >= 0,
                "Action ${action.id} should have non-negative impact estimate"
            )
        }
    }

    @Test
    fun allActionsHaveUniqueIds() {
        val ids = TinyActionCatalog.actions.map { it.id }
        val uniqueIds = ids.toSet()

        assertEquals(ids.size, uniqueIds.size, "All action IDs should be unique")
    }

    @Test
    fun getActionByIdReturnsCorrectAction() {
        val action = TinyActionCatalog.getActionById("transport_walk_short")

        assertNotNull(action, "Should find action by ID")
        assertEquals("transport_walk_short", action.id)
        assertEquals(Category.TRANSPORT, action.category)
    }

    @Test
    fun getActionByIdReturnsNullForUnknownId() {
        val action = TinyActionCatalog.getActionById("unknown_action_id")

        assertEquals(null, action, "Should return null for unknown ID")
    }

    @Test
    fun getActionsByCategoryReturnsOnlyMatchingCategory() {
        val transportActions = TinyActionCatalog.getActionsByCategory(Category.TRANSPORT)

        transportActions.forEach { action ->
            assertEquals(
                Category.TRANSPORT,
                action.category,
                "All returned actions should be TRANSPORT category"
            )
        }
    }

    // ========== FOOD ACTIONS TESTS ==========

    @Test
    fun foodCategoryHasDietaryOptions() {
        val foodActions = TinyActionCatalog.getActionsByCategory(Category.FOOD)
        val actionIds = foodActions.map { it.id }

        assertTrue(
            actionIds.any { it.contains("meatless") || it.contains("veg") || it.contains("plant") },
            "Food category should include vegetarian/vegan options"
        )
    }

    // ========== TRANSPORT ACTIONS TESTS ==========

    @Test
    fun transportCategoryHasLowEmissionOptions() {
        val transportActions = TinyActionCatalog.getActionsByCategory(Category.TRANSPORT)
        val actionIds = transportActions.map { it.id }

        assertTrue(
            actionIds.any { it.contains("walk") || it.contains("bike") || it.contains("public") },
            "Transport category should include low-emission options"
        )
    }

    // ========== ENERGY ACTIONS TESTS ==========

    @Test
    fun energyCategoryHasEasyActions() {
        val energyActions = TinyActionCatalog.getActionsByCategory(Category.ENERGY)

        assertTrue(
            energyActions.any { it.effortScore <= 2 },
            "Energy category should have at least one easy action (effort <= 2)"
        )
    }

    // ========== ACTION DATA QUALITY TESTS ==========

    @Test
    fun allActionsHaveTitle() {
        TinyActionCatalog.actions.forEach { action ->
            assertTrue(
                action.title.isNotBlank(),
                "Action ${action.id} should have a non-blank title"
            )
        }
    }

    @Test
    fun allActionsHaveDescription() {
        TinyActionCatalog.actions.forEach { action ->
            assertTrue(
                action.description.isNotBlank(),
                "Action ${action.id} should have a non-blank description"
            )
        }
    }

    // ========== IMPACT vs EFFORT BALANCE TESTS ==========

    @Test
    fun highImpactActionsExist() {
        val highImpactActions = TinyActionCatalog.actions.filter {
            it.impactEstimateCo2e >= 2.0
        }

        assertTrue(
            highImpactActions.isNotEmpty(),
            "Should have at least one high-impact action (>= 2.0 kgCO2e)"
        )
    }

    @Test
    fun lowEffortActionsExist() {
        val lowEffortActions = TinyActionCatalog.actions.filter {
            it.effortScore == 1
        }

        assertTrue(
            lowEffortActions.isNotEmpty(),
            "Should have at least one low-effort action (effort = 1)"
        )
    }

    @Test
    fun bestRatioActionExists() {
        val bestRatioAction = TinyActionCatalog.actions.maxByOrNull {
            it.impactEstimateCo2e / it.effortScore
        }

        assertNotNull(bestRatioAction, "Should be able to find best ratio action")
        assertTrue(
            bestRatioAction.impactEstimateCo2e / bestRatioAction.effortScore > 0.5,
            "Best ratio action should have meaningful impact/effort ratio"
        )
    }
}

