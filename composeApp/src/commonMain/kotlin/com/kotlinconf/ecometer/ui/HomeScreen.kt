package com.kotlinconf.ecometer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kotlinconf.ecometer.data.ActivityRepository
import com.kotlinconf.ecometer.data.EmissionFactorRepository
import com.kotlinconf.ecometer.data.LocalStorageProvider
import com.kotlinconf.ecometer.domain.ActivityEntry
import com.kotlinconf.ecometer.domain.Category
import com.kotlinconf.ecometer.domain.EmissionFactor
import com.kotlinconf.ecometer.domain.ReportGenerator
import com.kotlinconf.ecometer.domain.TinyActionEngine
import com.kotlinconf.ecometer.domain.Badge
import ecometerco2.composeapp.generated.resources.Res
import ecometerco2.composeapp.generated.resources.logo_app
import org.jetbrains.compose.resources.painterResource

enum class Screen {
    HOME,
    ADD_ACTIVITY,
    REPORT,
    SETTINGS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    storageProvider: LocalStorageProvider
) {
    // Shared repositories with persistence
    val activityRepository = remember { ActivityRepository(storageProvider) }
    val factorRepository = remember { EmissionFactorRepository() }
    val tinyActionEngine = remember { TinyActionEngine(activityRepository, storageProvider) }

    // Load persisted data on startup
    LaunchedEffect(Unit) {
        activityRepository.loadFromStorage()
        tinyActionEngine.loadFromStorage()
    }

    // Navigation state with back stack
    var navigationStack by remember { mutableStateOf(listOf(Screen.HOME)) }
    val currentScreen = navigationStack.last()

    // Navigate function
    val navigateTo: (Screen) -> Unit = { screen ->
        navigationStack = navigationStack + screen
    }

    // Navigate back function
    val navigateBack: () -> Unit = {
        if (navigationStack.size > 1) {
            navigationStack = navigationStack.dropLast(1)
        }
    }

    // Handle back press with BackHandler
    BackHandler(enabled = navigationStack.size > 1) {
        navigateBack()
    }

    when (currentScreen) {
        Screen.HOME -> MainDashboard(
            activityRepository = activityRepository,
            factorRepository = factorRepository,
            tinyActionEngine = tinyActionEngine,
            onNavigateToAddActivity = { navigateTo(Screen.ADD_ACTIVITY) },
            onNavigateToReport = { navigateTo(Screen.REPORT) },
            onNavigateToSettings = { navigateTo(Screen.SETTINGS) }
        )
        Screen.ADD_ACTIVITY -> AddActivityScreen(
            activityRepository = activityRepository,
            factorRepository = factorRepository,
            onActivityAdded = { navigateBack() },
            onNavigateBack = { navigateBack() }
        )
        Screen.REPORT -> ReportScreen(
            activityRepository = activityRepository,
            onNavigateBack = { navigateBack() }
        )
        Screen.SETTINGS -> SettingsScreen(
            activityRepository = activityRepository,
            onNavigateBack = { navigateBack() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboard(
    activityRepository: ActivityRepository,
    factorRepository: EmissionFactorRepository,
    tinyActionEngine: TinyActionEngine,
    onNavigateToAddActivity: () -> Unit,
    onNavigateToReport: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val reportGenerator = remember { ReportGenerator(activityRepository) }

    // Get fresh data on every recomposition
    val entries = activityRepository.getAllEntries()
    val weeklyReport = reportGenerator.generateWeeklyReport()

    // Tiny Action state
    var todayAction by remember { mutableStateOf(tinyActionEngine.getTodayAction()) }
    var actionCompleted by remember { mutableStateOf<Boolean?>(null) }
    val earnedBadges = remember(actionCompleted) { tinyActionEngine.getEarnedBadges() }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.logo_app),
                            contentDescription = "EcoMeterCO2 Logo",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        )
                        Text("EcoMeterCO2")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddActivity,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Activity"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Quick Stats Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                onClick = onNavigateToReport
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "This Week",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "${formatNumber(weeklyReport.totalEmissions, 1)} kg CO2e",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "${weeklyReport.entryCount} activities • Tap to see report",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Today's Tiny Action Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (actionCompleted == true)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Tiny Action",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Today's Tiny Action",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        if (earnedBadges.isNotEmpty()) {
                            Text(
                                text = earnedBadges.joinToString(" ") { it.symbol },
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = todayAction.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = todayAction.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "Saves ~${formatNumber(todayAction.impactEstimateCo2e, 2)} kg CO2e",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (actionCompleted == null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    tinyActionEngine.recordActionResult(todayAction.id, false)
                                    actionCompleted = false
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Not today")
                            }
                            Button(
                                onClick = {
                                    tinyActionEngine.recordActionResult(todayAction.id, true)
                                    actionCompleted = true
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Done",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text("Done!")
                                }
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (actionCompleted == true) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Success",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = if (actionCompleted == true) "Great job! Keep it up!" else "No worries, try again tomorrow!",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionCard(
                    icon = Icons.Filled.DateRange,
                    iconColor = Color(0xFF4CAF50),
                    label = "Transport",
                    onClick = onNavigateToAddActivity,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCard(
                    icon = Icons.Filled.Favorite,
                    iconColor = Color(0xFFFF9800),
                    label = "Food",
                    onClick = onNavigateToAddActivity,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCard(
                    icon = Icons.Filled.Warning,
                    iconColor = Color(0xFF2196F3),
                    label = "Energy",
                    onClick = onNavigateToAddActivity,
                    modifier = Modifier.weight(1f)
                )
            }

            // Recent Activities
            Text(
                text = "Recent Activities",
                style = MaterialTheme.typography.titleMedium
            )

            if (entries.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = "No activities recorded yet.\nPress + to get started!",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp).fillMaxWidth()
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(entries.reversed().take(10)) { entry ->
                        ActivityEntryCard(entry)
                    }
                }
            }

            // View Report Button
            OutlinedButton(
                onClick = onNavigateToReport,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Report",
                        modifier = Modifier.size(18.dp)
                    )
                    Text("View Full Report")
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(
    icon: ImageVector,
    iconColor: Color,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun ActivityEntryCard(entry: ActivityEntry) {
    var showDetails by remember { mutableStateOf(false) }

    Card(
        onClick = { showDetails = !showDetails },
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category icon with colored background
                    val (icon, iconColor) = when (entry.category) {
                        Category.TRANSPORT -> Icons.Filled.DateRange to Color(0xFF4CAF50)
                        Category.FOOD -> Icons.Filled.Favorite to Color(0xFFFF9800)
                        Category.ENERGY -> Icons.Filled.Warning to Color(0xFF2196F3)
                        Category.PURCHASES -> Icons.Filled.ShoppingCart to Color(0xFF9C27B0)
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(iconColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = entry.category.name,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = entry.factorId.replace("_", " ").replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = if (showDetails) "Tap to hide details" else "Tap to see how we calculated",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    text = "${formatNumber(entry.co2eTotal)} kg",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Transparency section - shows calculation details
            if (showDetails) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Calculation: ${entry.explanation}",
                            style = MaterialTheme.typography.bodySmall
                        )
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
    }
}

// Keep backward compatibility
@Composable
fun FactorCard(factor: EmissionFactor) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = factor.name,
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${factor.co2ePerUnit} kgCO2e / ${factor.unit}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = factor.source,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
