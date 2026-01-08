package com.kotlinconf.ecometer.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for Badge system
 */
class BadgeTest {

    // ========== BADGE ENUM TESTS ==========

    @Test
    fun allBadgesHaveTitles() {
        Badge.values().forEach { badge ->
            assertTrue(
                badge.title.isNotBlank(),
                "Badge ${badge.name} should have a non-blank title"
            )
        }
    }

    @Test
    fun allBadgesHaveDescriptions() {
        Badge.values().forEach { badge ->
            assertTrue(
                badge.description.isNotBlank(),
                "Badge ${badge.name} should have a non-blank description"
            )
        }
    }

    @Test
    fun allBadgesHaveEmojis() {
        Badge.values().forEach { badge ->
            assertTrue(
                badge.emoji.isNotBlank(),
                "Badge ${badge.name} should have a non-blank emoji"
            )
        }
    }

    @Test
    fun hasFirstActionBadge() {
        val badge = Badge.FIRST_ACTION

        assertNotNull(badge)
        assertEquals("First Step", badge.title)
        assertEquals("*", badge.symbol)
    }

    @Test
    fun hasThreeDayStreakBadge() {
        val badge = Badge.THREE_DAY_STREAK

        assertNotNull(badge)
        assertEquals("On a Roll", badge.title)
        assertEquals("~", badge.symbol)
    }

    @Test
    fun hasOneWeekChampionBadge() {
        val badge = Badge.ONE_WEEK_CHAMPION

        assertNotNull(badge)
        assertEquals("Week Champion", badge.title)
        assertEquals("#", badge.symbol)
    }

    @Test
    fun badgesAreDistinct() {
        val badges = Badge.values().toSet()

        assertEquals(3, badges.size, "Should have exactly 3 badges")
    }
}

