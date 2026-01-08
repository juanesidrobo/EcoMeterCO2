package com.kotlinconf.ecometer.domain

import kotlinx.serialization.Serializable

/**
 * Represents a "Tiny Action" - a small personalized recommendation
 * for reducing carbon footprint.
 */
@Serializable
data class TinyAction(
    val id: String,
    val title: String,
    val description: String,
    val category: Category,
    val impactEstimateCo2e: Double, // Estimated CO2e reduction in kg
    val effortScore: Int // 1-5, where 1 is easiest
)

/**
 * User's history of tiny action completions
 */
@Serializable
data class TinyActionRecord(
    val actionId: String,
    val completed: Boolean,
    val timestamp: String
)

/**
 * Tiny Action catalog with predefined actions
 */
object TinyActionCatalog {

    val actions = listOf(
        // Transport actions
        TinyAction(
            id = "transport_walk_short",
            title = "Walk for short trips",
            description = "Walk instead of driving for trips under 1 km today",
            category = Category.TRANSPORT,
            impactEstimateCo2e = 0.17,
            effortScore = 2
        ),
        TinyAction(
            id = "transport_bike",
            title = "Bike to work",
            description = "Use a bicycle for your commute today",
            category = Category.TRANSPORT,
            impactEstimateCo2e = 1.7,
            effortScore = 3
        ),
        TinyAction(
            id = "transport_public",
            title = "Take public transit",
            description = "Use bus or metro instead of car today",
            category = Category.TRANSPORT,
            impactEstimateCo2e = 0.8,
            effortScore = 2
        ),
        TinyAction(
            id = "transport_carpool",
            title = "Carpool today",
            description = "Share your ride with a colleague or neighbor",
            category = Category.TRANSPORT,
            impactEstimateCo2e = 0.85,
            effortScore = 3
        ),

        // Food actions
        TinyAction(
            id = "food_meatless_meal",
            title = "Have a meatless meal",
            description = "Choose a vegetarian option for one meal today",
            category = Category.FOOD,
            impactEstimateCo2e = 2.0,
            effortScore = 1
        ),
        TinyAction(
            id = "food_swap_beef_chicken",
            title = "Swap beef for chicken",
            description = "Choose chicken instead of beef for your next meal",
            category = Category.FOOD,
            impactEstimateCo2e = 4.8,
            effortScore = 1
        ),
        TinyAction(
            id = "food_plant_milk",
            title = "Try plant-based milk",
            description = "Use oat or soy milk in your coffee today",
            category = Category.FOOD,
            impactEstimateCo2e = 1.1,
            effortScore = 1
        ),
        TinyAction(
            id = "food_local_meal",
            title = "Eat local today",
            description = "Choose locally sourced ingredients for one meal",
            category = Category.FOOD,
            impactEstimateCo2e = 0.5,
            effortScore = 2
        ),

        // Energy actions
        TinyAction(
            id = "energy_unplug",
            title = "Unplug idle devices",
            description = "Unplug chargers and devices not in use",
            category = Category.ENERGY,
            impactEstimateCo2e = 0.2,
            effortScore = 1
        ),
        TinyAction(
            id = "energy_lights_off",
            title = "Turn off lights",
            description = "Turn off lights when leaving rooms",
            category = Category.ENERGY,
            impactEstimateCo2e = 0.3,
            effortScore = 1
        ),
        TinyAction(
            id = "energy_shorter_shower",
            title = "Take a shorter shower",
            description = "Reduce your shower time by 2 minutes",
            category = Category.ENERGY,
            impactEstimateCo2e = 0.4,
            effortScore = 2
        ),
        TinyAction(
            id = "energy_ac_adjust",
            title = "Adjust AC temperature",
            description = "Set AC 1-2 degrees higher (or lower for heating)",
            category = Category.ENERGY,
            impactEstimateCo2e = 0.9,
            effortScore = 1
        ),

        // Purchases actions
        TinyAction(
            id = "purchase_skip",
            title = "Skip an impulse buy",
            description = "Wait 24 hours before making a non-essential purchase",
            category = Category.PURCHASES,
            impactEstimateCo2e = 5.0,
            effortScore = 2
        ),
        TinyAction(
            id = "purchase_reusable_bag",
            title = "Use reusable bags",
            description = "Bring your own bags when shopping today",
            category = Category.PURCHASES,
            impactEstimateCo2e = 0.03,
            effortScore = 1
        ),
        TinyAction(
            id = "purchase_secondhand",
            title = "Buy secondhand",
            description = "Consider secondhand options for your next purchase",
            category = Category.PURCHASES,
            impactEstimateCo2e = 8.0,
            effortScore = 3
        )
    )

    fun getActionsByCategory(category: Category): List<TinyAction> {
        return actions.filter { it.category == category }
    }

    fun getActionById(id: String): TinyAction? {
        return actions.find { it.id == id }
    }
}

