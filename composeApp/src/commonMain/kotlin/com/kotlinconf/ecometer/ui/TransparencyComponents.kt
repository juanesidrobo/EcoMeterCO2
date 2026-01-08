package com.kotlinconf.ecometer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotlinconf.ecometer.domain.ActivityEntry

/**
 * Component showing transparency about how emissions were calculated
 */
@Composable
fun EmissionExplanationCard(
    entry: ActivityEntry,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        onClick = { expanded = !expanded },
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Info",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "How we estimated it",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = if (expanded) "(hide)" else "(show)",
                    style = MaterialTheme.typography.labelMedium
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Calculation:",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = entry.explanation,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Result: ${formatNumber(entry.co2eTotal, 3)} kg CO₂e",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Source: UK Gov GHG Conversion Factors 2025",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Detailed breakdown of emissions by factor
 */
@Composable
fun EmissionBreakdownCard(
    totalEmissions: Double,
    emissionsByCategory: Map<com.kotlinconf.ecometer.domain.Category, Double>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Report",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Emissions Breakdown",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (emissionsByCategory.isEmpty()) {
                Text(
                    text = "No emissions recorded yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                emissionsByCategory.forEach { (category, emissions) ->
                    val percentage = if (totalEmissions > 0) {
                        (emissions / totalEmissions * 100).toInt()
                    } else 0

                    val (icon, iconColor, label) = when (category) {
                        com.kotlinconf.ecometer.domain.Category.TRANSPORT -> Triple(Icons.Filled.DateRange, Color(0xFF4CAF50), "Transport")
                        com.kotlinconf.ecometer.domain.Category.FOOD -> Triple(Icons.Filled.Favorite, Color(0xFFFF9800), "Food")
                        com.kotlinconf.ecometer.domain.Category.ENERGY -> Triple(Icons.Filled.Warning, Color(0xFF2196F3), "Energy")
                        com.kotlinconf.ecometer.domain.Category.PURCHASES -> Triple(Icons.Filled.ShoppingCart, Color(0xFF9C27B0), "Purchases")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
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
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Text(
                            text = "${formatNumber(emissions, 1)} kg ($percentage%)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Simple progress bar
                    LinearProgressIndicator(
                        progress = { (percentage / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(4.dp),
                        color = when (category) {
                            com.kotlinconf.ecometer.domain.Category.TRANSPORT -> MaterialTheme.colorScheme.primary
                            com.kotlinconf.ecometer.domain.Category.FOOD -> MaterialTheme.colorScheme.secondary
                            com.kotlinconf.ecometer.domain.Category.ENERGY -> MaterialTheme.colorScheme.tertiary
                            com.kotlinconf.ecometer.domain.Category.PURCHASES -> MaterialTheme.colorScheme.error
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${formatNumber(totalEmissions, 1)} kg CO₂e",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

