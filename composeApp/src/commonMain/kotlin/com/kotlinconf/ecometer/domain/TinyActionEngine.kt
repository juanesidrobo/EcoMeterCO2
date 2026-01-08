package com.kotlinconf.ecometer.domain

import com.kotlinconf.ecometer.data.ActivityRepository
import com.kotlinconf.ecometer.data.LocalStorageProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Engine for recommending personalized "Tiny Actions" based on user behavior.
 * Uses a simple scoring system with epsilon-greedy exploration.
 */
class TinyActionEngine(
    private val activityRepository: ActivityRepository,
    private val storageProvider: LocalStorageProvider? = null
) {
    private val completedActions = mutableListOf<TinyActionRecord>()
    private val epsilon = 0.2 // 20% exploration rate
    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * Load persisted tiny action history
     */
    suspend fun loadFromStorage() {
        storageProvider?.let { storage ->
            val savedRecords = storage.loadTinyActionHistory()
            completedActions.clear()
            completedActions.addAll(savedRecords)
        }
    }

    /**
     * Save tiny action history to persistent storage
     */
    private fun saveToStorage() {
        storageProvider?.let { storage ->
            scope.launch {
                storage.saveTinyActionHistory(completedActions.toList())
            }
        }
    }

    /**
     * Get today's recommended tiny action based on user's dominant category
     * and historical performance.
     */
    fun getTodayAction(): TinyAction {
        val dominantCategory = findDominantCategory()
        val candidates = getCandidateActions(dominantCategory)

        // Epsilon-greedy: 80% exploit best action, 20% explore
        return if (Random.nextFloat() < epsilon) {
            // Explore: pick random action
            candidates.random()
        } else {
            // Exploit: pick highest scoring action
            candidates.maxByOrNull { scoreAction(it) } ?: candidates.first()
        }
    }

    /**
     * Find the category with highest emissions in the last 7 days.
     */
    private fun findDominantCategory(): Category {
        val emissionsByCategory = activityRepository.getEmissionsByCategoryForLastDays(7)

        return if (emissionsByCategory.isEmpty()) {
            // Default to transport if no data
            Category.TRANSPORT
        } else {
            emissionsByCategory.maxByOrNull { it.value }?.key ?: Category.TRANSPORT
        }
    }

    /**
     * Get candidate actions for a category, plus some from other categories.
     */
    private fun getCandidateActions(dominantCategory: Category): List<TinyAction> {
        val primaryActions = TinyActionCatalog.getActionsByCategory(dominantCategory)
        val otherActions = TinyActionCatalog.actions
            .filter { it.category != dominantCategory }
            .shuffled()
            .take(2)

        return (primaryActions + otherActions).ifEmpty { TinyActionCatalog.actions }
    }

    /**
     * Score an action based on impact/effort ratio and personal fit.
     */
    private fun scoreAction(action: TinyAction): Double {
        val baseScore = action.impactEstimateCo2e / action.effortScore
        val personalFit = calculatePersonalFit(action)
        return baseScore * personalFit
    }

    /**
     * Calculate personal fit based on historical completion rate for similar actions.
     */
    private fun calculatePersonalFit(action: TinyAction): Double {
        val categoryRecords = completedActions.filter { record ->
            TinyActionCatalog.getActionById(record.actionId)?.category == action.category
        }

        if (categoryRecords.isEmpty()) {
            return 1.0 // No history, neutral fit
        }

        val completionRate = categoryRecords.count { it.completed }.toDouble() / categoryRecords.size
        // Boost score if user completes similar actions, reduce if they skip
        return 0.5 + completionRate
    }

    /**
     * Record that user completed or skipped an action.
     */
    fun recordActionResult(actionId: String, completed: Boolean) {
        completedActions.add(
            TinyActionRecord(
                actionId = actionId,
                completed = completed,
                timestamp = kotlinx.datetime.Clock.System.now().toString()
            )
        )
        saveToStorage()
    }

    /**
     * Get current streak (consecutive days with completed actions).
     */
    fun getCurrentStreak(): Int {
        val completedRecords = completedActions.filter { it.completed }
        if (completedRecords.isEmpty()) return 0

        // Simplified: count recent completed actions as streak indicator
        return completedRecords.takeLast(7).count { it.completed }
    }

    /**
     * Get total completed actions count.
     */
    fun getTotalCompleted(): Int {
        return completedActions.count { it.completed }
    }

    /**
     * Check and return any earned badges.
     */
    fun getEarnedBadges(): List<Badge> {
        val earned = mutableListOf<Badge>()
        val totalCompleted = getTotalCompleted()
        val streak = getCurrentStreak()

        if (totalCompleted >= 1) {
            earned.add(Badge.FIRST_ACTION)
        }
        if (streak >= 3) {
            earned.add(Badge.THREE_DAY_STREAK)
        }
        if (totalCompleted >= 7) {
            earned.add(Badge.ONE_WEEK_CHAMPION)
        }

        return earned
    }
}

/**
 * Fixed badges for gamification
 * Using web-safe symbols instead of emojis (Skiko/Wasm doesn't render emojis)
 */
enum class Badge(val title: String, val description: String, val symbol: String) {
    FIRST_ACTION("First Step", "Completed your first tiny action", "*"),
    THREE_DAY_STREAK("On a Roll", "3-day streak of tiny actions", "~"),
    ONE_WEEK_CHAMPION("Week Champion", "Completed 7 tiny actions", "#")
}

