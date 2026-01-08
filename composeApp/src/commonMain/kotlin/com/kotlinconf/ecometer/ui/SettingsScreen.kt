package com.kotlinconf.ecometer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kotlinconf.ecometer.data.ActivityRepository
import ecometerco2.composeapp.generated.resources.Res
import ecometerco2.composeapp.generated.resources.logo_app
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    activityRepository: ActivityRepository,
    onNavigateBack: () -> Unit
) {
    var exportedData by remember { mutableStateOf<String?>(null) }
    var importText by remember { mutableStateOf("") }
    var showImportDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showClearConfirmDialog by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                        Text("Settings")
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
            // App Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // App Logo
                    Image(
                        painter = painterResource(Res.drawable.logo_app),
                        contentDescription = "EcoMeterCO2 Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "# EcoMeterCO2",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "Version 1.0.0",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Track your daily CO2e footprint and get personalized tiny actions to reduce your impact.",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Data Management Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Data Management",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Data Management",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Export Data
            Card(
                onClick = {
                    scope.launch {
                        exportedData = activityRepository.exportData()
                        if (exportedData != null) {
                            showExportDialog = true
                        } else {
                            statusMessage = "Export not available"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Export Data",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Export all your data as JSON",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Export"
                    )
                }
            }

            // Import Data
            Card(
                onClick = { showImportDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Import Data",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Import data from JSON (replaces current data)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Import"
                    )
                }
            }

            // Clear All Data
            Card(
                onClick = { showClearConfirmDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Column {
                            Text(
                                text = "Clear All Data",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = "Delete all activity entries",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            // About Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "About",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Methodology",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "EcoMeterCO2 uses emission factors from UK Government GHG Conversion Factors 2025 and other open datasets to estimate CO₂e emissions.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Data Sources",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "• UK Gov GHG Conversion Factors 2025\n• Poore & Nemecek 2018 (Food)\n• EPA GHG Emission Factors Hub",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Privacy",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "All data is stored locally on your device. No account required. Your data never leaves your device unless you export it.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Status Message
            statusMessage?.let { msg ->
                Text(
                    text = msg,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // Export Dialog
    if (showExportDialog && exportedData != null) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("📤 Exported Data") },
            text = {
                Column {
                    Text(
                        text = "Copy the JSON below:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = exportedData!!,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp),
                        readOnly = true,
                        maxLines = 10
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Import Dialog
    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = { Text("📥 Import Data") },
            text = {
                Column {
                    Text(
                        text = "Paste your JSON data below. This will REPLACE all current data.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = importText,
                        onValueChange = { importText = it },
                        modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp),
                        placeholder = { Text("Paste JSON here...") },
                        maxLines = 10
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            val success = activityRepository.importData(importText)
                            statusMessage = if (success) {
                                "Data imported successfully!"
                            } else {
                                "Import failed. Check JSON format."
                            }
                            showImportDialog = false
                            importText = ""
                        }
                    }
                ) {
                    Text("Import")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImportDialog = false
                    importText = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Clear Confirmation Dialog
    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            title = { Text("Clear All Data?") },
            text = {
                Text("This will permanently delete all your activity entries. This action cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        activityRepository.clearAll()
                        statusMessage = "All data cleared"
                        showClearConfirmDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

