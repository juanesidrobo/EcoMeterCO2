package com.kotlinconf.ecometer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kotlinconf.ecometer.data.ActivityRepository
import com.kotlinconf.ecometer.data.EmissionFactorRepository
import com.kotlinconf.ecometer.domain.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(
    activityRepository: ActivityRepository,
    factorRepository: EmissionFactorRepository,
    onActivityAdded: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedFactor by remember { mutableStateOf<EmissionFactor?>(null) }
    var amountText by remember { mutableStateOf("") }
    var factors by remember { mutableStateOf<List<EmissionFactor>>(emptyList()) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Load factors
    LaunchedEffect(Unit) {
        factors = factorRepository.getFactors()
    }

    // Filter factors by category
    val filteredFactors = remember(selectedCategory, factors) {
        if (selectedCategory != null) {
            factors.filter { it.category == selectedCategory }
        } else {
            factors
        }
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
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add"
                        )
                        Text("Add Activity")
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
            // Category Selection
            Text(
                text = "1. Select a category",
                style = MaterialTheme.typography.titleMedium
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        onClick = {
                            selectedCategory = Category.TRANSPORT
                            selectedFactor = null
                        },
                        label = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.DateRange,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text("Transport")
                            }
                        },
                        selected = selectedCategory == Category.TRANSPORT,
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        onClick = {
                            selectedCategory = Category.FOOD
                            selectedFactor = null
                        },
                        label = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text("Food")
                            }
                        },
                        selected = selectedCategory == Category.FOOD,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        onClick = {
                            selectedCategory = Category.ENERGY
                            selectedFactor = null
                        },
                        label = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Warning,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text("Energy")
                            }
                        },
                        selected = selectedCategory == Category.ENERGY,
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        onClick = {
                            selectedCategory = Category.PURCHASES
                            selectedFactor = null
                        },
                        label = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ShoppingCart,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text("Purchases")
                            }
                        },
                        selected = selectedCategory == Category.PURCHASES,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Factor Selection
            if (selectedCategory != null) {
                Text(
                    text = "2. Select activity type",
                    style = MaterialTheme.typography.titleMedium
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filteredFactors.forEach { factor ->
                        Card(
                            onClick = { selectedFactor = factor },
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedFactor == factor)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = factor.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "${factor.co2ePerUnit} kgCO2e por ${factor.unit}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Amount Input
            if (selectedFactor != null) {
                Text(
                    text = "3. Enter the amount",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it.filter { char -> char.isDigit() || char == '.' }
                        showError = false
                    },
                    label = { Text("Amount (${selectedFactor!!.unit})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError,
                    supportingText = if (showError) {
                        { Text(errorMessage) }
                    } else null
                )

                // Preview of emissions
                val amount = amountText.toDoubleOrNull() ?: 0.0
                if (amount > 0) {
                    val estimatedEmission = amount * selectedFactor!!.co2ePerUnit

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Estimated emission",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = "${formatNumber(estimatedEmission)} kg CO2e",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button
                Button(
                    onClick = {
                        val amount = amountText.toDoubleOrNull()
                        if (amount == null || amount <= 0) {
                            showError = true
                            errorMessage = "Please enter a valid amount"
                            return@Button
                        }

                        val entry = ActivityEntry.create(
                            category = selectedCategory!!,
                            amount = amount,
                            factor = selectedFactor!!
                        )

                        activityRepository.addEntry(entry)
                        onActivityAdded()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = amountText.isNotEmpty() && (amountText.toDoubleOrNull() ?: 0.0) > 0
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add"
                        )
                        Text("Add Activity")
                    }
                }
            }
        }
    }
}

