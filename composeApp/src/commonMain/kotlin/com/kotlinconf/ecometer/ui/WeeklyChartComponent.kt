package com.kotlinconf.ecometer.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kotlinconf.ecometer.data.ActivityRepository
import com.kotlinconf.ecometer.domain.Category

/**
 * Data class representing a day's emissions for the chart
 */
data class DayEmission(
    val dayLabel: String,
    val totalEmissions: Double,
    val emissionsByCategory: Map<Category, Double>
)

/**
 * A visual 7-day bar chart showing daily emissions
 */
@Composable
fun WeeklyChartCard(
    activityRepository: ActivityRepository,
    modifier: Modifier = Modifier
) {
    val dayData = remember(activityRepository.getAllEntries().size) {
        calculateWeeklyData(activityRepository)
    }

    val maxEmission = dayData.maxOfOrNull { it.totalEmissions } ?: 1.0

    val transportColor = MaterialTheme.colorScheme.primary
    val foodColor = MaterialTheme.colorScheme.secondary
    val energyColor = MaterialTheme.colorScheme.tertiary
    val purchasesColor = MaterialTheme.colorScheme.error

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Chart",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "7-Day Trend",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LegendItem(color = transportColor, label = "Transport")
                LegendItem(color = foodColor, label = "Food")
                LegendItem(color = energyColor, label = "Energy")
                LegendItem(color = purchasesColor, label = "Purchases")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bar Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val barWidth = size.width / (dayData.size * 2f)
                    val chartHeight = size.height - 40f // Leave space for labels

                    dayData.forEachIndexed { index, dayEmission ->
                        val x = (index * 2 + 0.5f) * barWidth

                        // Draw stacked bar
                        var currentY = chartHeight

                        // Transport
                        val transportHeight = if (maxEmission > 0)
                            (dayEmission.emissionsByCategory[Category.TRANSPORT] ?: 0.0) / maxEmission * chartHeight
                        else 0.0
                        if (transportHeight > 0) {
                            drawRect(
                                color = transportColor,
                                topLeft = Offset(x, (currentY - transportHeight).toFloat()),
                                size = Size(barWidth, transportHeight.toFloat())
                            )
                            currentY -= transportHeight.toFloat()
                        }

                        // Food
                        val foodHeight = if (maxEmission > 0)
                            (dayEmission.emissionsByCategory[Category.FOOD] ?: 0.0) / maxEmission * chartHeight
                        else 0.0
                        if (foodHeight > 0) {
                            drawRect(
                                color = foodColor,
                                topLeft = Offset(x, (currentY - foodHeight).toFloat()),
                                size = Size(barWidth, foodHeight.toFloat())
                            )
                            currentY -= foodHeight.toFloat()
                        }

                        // Energy
                        val energyHeight = if (maxEmission > 0)
                            (dayEmission.emissionsByCategory[Category.ENERGY] ?: 0.0) / maxEmission * chartHeight
                        else 0.0
                        if (energyHeight > 0) {
                            drawRect(
                                color = energyColor,
                                topLeft = Offset(x, (currentY - energyHeight).toFloat()),
                                size = Size(barWidth, energyHeight.toFloat())
                            )
                            currentY -= energyHeight.toFloat()
                        }

                        // Purchases
                        val purchasesHeight = if (maxEmission > 0)
                            (dayEmission.emissionsByCategory[Category.PURCHASES] ?: 0.0) / maxEmission * chartHeight
                        else 0.0
                        if (purchasesHeight > 0) {
                            drawRect(
                                color = purchasesColor,
                                topLeft = Offset(x, (currentY - purchasesHeight).toFloat()),
                                size = Size(barWidth, purchasesHeight.toFloat())
                            )
                        }
                    }
                }

                // Day labels below bars
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    dayData.forEach { dayEmission ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = dayEmission.dayLabel,
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = formatNumber(dayEmission.totalEmissions, 1),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Summary
            Spacer(modifier = Modifier.height(12.dp))

            val topCategory = dayData.flatMap { it.emissionsByCategory.entries }
                .groupBy { it.key }
                .mapValues { (_, entries) -> entries.sumOf { it.value } }
                .maxByOrNull { it.value }

            if (topCategory != null && topCategory.value > 0) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Top driver this week:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val (icon, iconColor, label) = when (topCategory.key) {
                                Category.TRANSPORT -> Triple(Icons.Filled.DateRange, Color(0xFF4CAF50), "Transport")
                                Category.FOOD -> Triple(Icons.Filled.Favorite, Color(0xFFFF9800), "Food")
                                Category.ENERGY -> Triple(Icons.Filled.Warning, Color(0xFF2196F3), "Energy")
                                Category.PURCHASES -> Triple(Icons.Filled.ShoppingCart, Color(0xFF9C27B0), "Purchases")
                            }
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(iconColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = label,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Canvas(modifier = Modifier.size(12.dp)) {
            drawRect(color = color)
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

/**
 * Calculate emission data for the last 7 days
 * Uses ActivityRepository's getEntriesFromLastDays and groups by date string
 */
private fun calculateWeeklyData(activityRepository: ActivityRepository): List<DayEmission> {
    val dayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    // Get all entries from last 7 days using repository method
    val allRecentEntries = activityRepository.getEntriesFromLastDays(7)

    // Group entries by date (YYYY-MM-DD from timestampIso)
    val entriesByDate = allRecentEntries.groupBy { entry ->
        entry.timestampIso.substring(0, 10) // Extract YYYY-MM-DD
    }

    // Get unique dates and sort them
    val uniqueDates = entriesByDate.keys.sorted()

    // If we have fewer than 7 days of data, pad with empty days
    // For display purposes, we'll show the available data
    val displayDates = if (uniqueDates.size >= 7) {
        uniqueDates.takeLast(7)
    } else {
        // Pad with empty days before the data we have
        val padding = (7 - uniqueDates.size)
        List(padding) { "" } + uniqueDates
    }

    return displayDates.mapIndexed { index, dateStr ->
        val dayEntries = if (dateStr.isNotEmpty()) {
            entriesByDate[dateStr] ?: emptyList()
        } else {
            emptyList()
        }

        // Calculate day of week from date string or use index as fallback
        val dayIndex = if (dateStr.isNotEmpty()) {
            calculateDayOfWeek(dateStr)
        } else {
            index
        }

        val emissionsByCategory = dayEntries
            .groupBy { it.category }
            .mapValues { (_, categoryEntries) -> categoryEntries.sumOf { it.co2eTotal } }

        DayEmission(
            dayLabel = dayLabels.getOrElse(dayIndex) { "Day" },
            totalEmissions = dayEntries.sumOf { it.co2eTotal },
            emissionsByCategory = emissionsByCategory
        )
    }
}

/**
 * Simple day of week calculation from ISO date string
 */
private fun calculateDayOfWeek(dateStr: String): Int {
    // Parse YYYY-MM-DD
    val parts = dateStr.split("-")
    if (parts.size != 3) return 0

    val year = parts[0].toIntOrNull() ?: return 0
    val month = parts[1].toIntOrNull() ?: return 0
    val day = parts[2].toIntOrNull() ?: return 0

    // Zeller's formula for day of week (modified for Monday = 0)
    val adjustedMonth = if (month < 3) month + 12 else month
    val adjustedYear = if (month < 3) year - 1 else year

    val q = day
    val m = adjustedMonth
    val k = adjustedYear % 100
    val j = adjustedYear / 100

    val h = (q + (13 * (m + 1)) / 5 + k + k / 4 + j / 4 - 2 * j) % 7

    // Convert from Zeller (0=Saturday) to our format (0=Monday)
    return ((h + 5) % 7)
}

