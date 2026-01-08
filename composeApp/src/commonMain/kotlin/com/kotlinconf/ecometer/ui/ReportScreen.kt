package com.kotlinconf.ecometer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kotlinconf.ecometer.data.ActivityRepository
import com.kotlinconf.ecometer.domain.Category
import com.kotlinconf.ecometer.domain.RegionalAverages
import com.kotlinconf.ecometer.domain.ReportGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    activityRepository: ActivityRepository,
    onNavigateBack: () -> Unit
) {
    val reportGenerator = remember { ReportGenerator(activityRepository) }
    var selectedPeriod by remember { mutableStateOf(7) } // 7 = weekly, 30 = monthly
    var selectedRegion by remember { mutableStateOf("global") }

    val report = remember(selectedPeriod, activityRepository.getAllEntries().size) {
        reportGenerator.generateReport(selectedPeriod)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Report"
                        )
                        Text("Impact Report")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Period Selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    onClick = { selectedPeriod = 7 },
                    label = { Text("Weekly") },
                    selected = selectedPeriod == 7,
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    onClick = { selectedPeriod = 30 },
                    label = { Text("Monthly") },
                    selected = selectedPeriod == 30,
                    modifier = Modifier.weight(1f)
                )
            }

            // 7-Day Visual Chart (only show for weekly view)
            if (selectedPeriod == 7) {
                WeeklyChartCard(activityRepository = activityRepository)
            }

            // Main Stats Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Emissions",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "${formatNumber(report.totalEmissions, 1)} kg CO2e",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "in the last ${report.periodDays} days",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "${report.entryCount} activities recorded",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Daily Average
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Daily Average",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "${formatNumber(report.averageDailyEmissions)} kg CO2e/day",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            // Comparison Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Compare",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Comparison with Averages",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Region Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("global", "eu", "usa", "latam").forEach { region ->
                    FilterChip(
                        onClick = { selectedRegion = region },
                        label = {
                            Text(
                                when (region) {
                                    "global" -> "Global"
                                    "eu" -> "EU"
                                    "usa" -> "USA"
                                    "latam" -> "LATAM"
                                    else -> region
                                },
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        selected = selectedRegion == region
                    )
                }
            }

            val regionalAverage = RegionalAverages.getDailyAverageForRegion(selectedRegion)
            val comparisonToRegion = RegionalAverages.compareToRegionalAverage(
                report.averageDailyEmissions,
                selectedRegion
            )

            // Regional Comparison Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        comparisonToRegion < 80 -> MaterialTheme.colorScheme.tertiaryContainer
                        comparisonToRegion > 120 -> MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when {
                            comparisonToRegion < 50 -> "Excellent!"
                            comparisonToRegion < 80 -> "Very good"
                            comparisonToRegion < 100 -> "Below average"
                            comparisonToRegion < 120 -> "Slightly above"
                            else -> "Above average"
                        },
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${formatNumber(comparisonToRegion, 0)}% of regional average",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "(Average: ${formatNumber(regionalAverage, 1)} kg CO2e/day)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Sustainable Target Comparison
            val sustainableComparison = RegionalAverages.compareToSustainableTarget(
                report.averageDailyEmissions
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        sustainableComparison <= 100 -> MaterialTheme.colorScheme.tertiaryContainer
                        else -> MaterialTheme.colorScheme.errorContainer
                    }
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sustainable Target (Paris)",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "${formatNumber(sustainableComparison, 0)}% of target",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "(Target: ${RegionalAverages.SUSTAINABLE_DAILY_LIMIT} kg CO2e/day)",
                        style = MaterialTheme.typography.bodySmall
                    )
                    if (sustainableComparison > 100) {
                        Text(
                            text = "Reduce ${formatNumber(report.averageDailyEmissions - RegionalAverages.SUSTAINABLE_DAILY_LIMIT, 1)} kg/day to reach the target",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // Emissions by Category
            if (report.emissionsByCategory.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Breakdown",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Breakdown by Category",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                report.emissionsByCategory.forEach { (category, emissions) ->
                    val (icon, iconColor, label) = when (category) {
                        Category.TRANSPORT -> Triple(Icons.Filled.DateRange, Color(0xFF4CAF50), "Transport")
                        Category.FOOD -> Triple(Icons.Filled.Favorite, Color(0xFFFF9800), "Food")
                        Category.ENERGY -> Triple(Icons.Filled.Warning, Color(0xFF2196F3), "Energy")
                        Category.PURCHASES -> Triple(Icons.Filled.ShoppingCart, Color(0xFF9C27B0), "Purchases")
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(iconColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Text(
                                text = "${formatNumber(emissions)} kg CO2e",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = "No activities recorded in this period.\nStart tracking your activities!",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp).fillMaxWidth()
                    )
                }
            }
        }
    }
}

