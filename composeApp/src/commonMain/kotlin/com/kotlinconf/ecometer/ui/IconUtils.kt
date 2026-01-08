package com.kotlinconf.ecometer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kotlinconf.ecometer.domain.Category
import com.kotlinconf.ecometer.domain.Badge

/**
 * Cross-platform icon utilities using Material Icons.
 * Material Icons work on all platforms (Android, Web/Wasm, iOS, Desktop)
 * because they are rendered as vector graphics, not emoji fonts.
 */
object CategoryIcons {
    // App branding
    const val APP_NAME = "EcoMeterCO2"
    const val APP_TITLE = "EcoMeterCO2"

    // Labels for display
    const val TRANSPORT_LABEL = "Transport"
    const val FOOD_LABEL = "Food"
    const val ENERGY_LABEL = "Energy"
    const val PURCHASES_LABEL = "Purchases"
    const val SETTINGS_LABEL = "Settings"
    const val REPORT_LABEL = "Report"
}

/**
 * Icon data class to hold icon info
 */
data class IconInfo(
    val icon: ImageVector,
    val color: Color,
    val label: String
)

/**
 * Get icon info for a category
 */
fun getCategoryIconInfo(category: Category): IconInfo {
    return when (category) {
        Category.TRANSPORT -> IconInfo(
            icon = Icons.Filled.DateRange,
            color = Color(0xFF4CAF50),
            label = "T"
        )
        Category.FOOD -> IconInfo(
            icon = Icons.Filled.Favorite,
            color = Color(0xFFFF9800),
            label = "F"
        )
        Category.ENERGY -> IconInfo(
            icon = Icons.Filled.Warning,
            color = Color(0xFF2196F3),
            label = "E"
        )
        Category.PURCHASES -> IconInfo(
            icon = Icons.Filled.ShoppingCart,
            color = Color(0xFF9C27B0),
            label = "P"
        )
    }
}

/**
 * Get icon for a category - returns a composable that works on all platforms
 */
@Composable
fun CategoryIcon(
    category: Category,
    size: Dp = 32.dp,
    useCircleBackground: Boolean = true
) {
    val iconInfo = getCategoryIconInfo(category)

    if (useCircleBackground) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(iconInfo.color),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconInfo.icon,
                contentDescription = getCategoryLabel(category),
                tint = Color.White,
                modifier = Modifier.size(size * 0.6f)
            )
        }
    } else {
        Icon(
            imageVector = iconInfo.icon,
            contentDescription = getCategoryLabel(category),
            tint = iconInfo.color,
            modifier = Modifier.size(size)
        )
    }
}

/**
 * Get the display label for a category
 */
fun getCategoryLabel(category: Category): String {
    return when (category) {
        Category.TRANSPORT -> "Transport"
        Category.FOOD -> "Food"
        Category.ENERGY -> "Energy"
        Category.PURCHASES -> "Purchases"
    }
}

/**
 * Get badge icon info
 */
fun getBadgeIconInfo(badge: Badge): IconInfo {
    return when (badge) {
        Badge.FIRST_ACTION -> IconInfo(
            icon = Icons.Filled.Star,
            color = Color(0xFF4CAF50),
            label = "1st"
        )
        Badge.THREE_DAY_STREAK -> IconInfo(
            icon = Icons.Filled.Favorite,
            color = Color(0xFFFF5722),
            label = "3d"
        )
        Badge.ONE_WEEK_CHAMPION -> IconInfo(
            icon = Icons.Filled.Star,
            color = Color(0xFFFFD700),
            label = "7d"
        )
    }
}

/**
 * Get badge icon that works on all platforms
 */
@Composable
fun BadgeIcon(
    badge: Badge,
    size: Dp = 24.dp
) {
    val iconInfo = getBadgeIconInfo(badge)

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(iconInfo.color),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = iconInfo.icon,
            contentDescription = badge.title,
            tint = Color.White,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

/**
 * Simple colored category indicator
 */
@Composable
fun CategoryIndicator(
    category: Category,
    size: Dp = 12.dp
) {
    val color = getCategoryIconInfo(category).color

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}

/**
 * Common app icons - Material Icons that work everywhere
 */
object AppIcons {
    val Home = Icons.Filled.Home
    val Add = Icons.Filled.Add
    val Settings = Icons.Filled.Settings
    val Report = Icons.Filled.DateRange
    val Info = Icons.Filled.Info
    val Check = Icons.Filled.CheckCircle
}
