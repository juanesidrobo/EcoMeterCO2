package com.kotlinconf.ecometer.domain

import kotlinx.serialization.Serializable
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
@Serializable
data class ActivityEntry(
    val id: String,

    // Save as String to serialization
    val timestampIso: String,

    val category: Category,
    val amount: Double,
    val factorId: String,

    val co2eTotal: Double,
    val explanation: String
) {
    // Convert the String to Instant real
    val timestamp: Instant
        get() = Instant.parse(timestampIso)

    companion object {
        fun create(
            category: Category,
            amount: Double,
            factor: EmissionFactor
        ): ActivityEntry {
            val now = Clock.System.now()

            return ActivityEntry(
                id = "${now.toEpochMilliseconds()}-${category.name}",
                timestampIso = now.toString(),
                category = category,
                amount = amount,
                factorId = factor.id,
                co2eTotal = amount * factor.co2ePerUnit,
                explanation = "$amount ${factor.unit} × ${factor.co2ePerUnit} kgCO2e"
            )
        }
    }
}
