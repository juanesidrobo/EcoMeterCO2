package com.kotlinconf.ecometer.domain

import kotlinx.serialization.Serializable
import com.kotlinconf.ecometer.util.currentTimeMillis
import com.kotlinconf.ecometer.util.currentTimeIso
import com.kotlinconf.ecometer.util.parseIsoToMillis

@Serializable
data class ActivityEntry(
    val id: String,

    // Save as String for serialization
    val timestampIso: String,

    val category: Category,
    val amount: Double,
    val factorId: String,

    val co2eTotal: Double,
    val explanation: String
) {
    // Get timestamp as epoch millis
    val timestampMillis: Long
        get() = parseIsoToMillis(timestampIso)

    companion object {
        fun create(
            category: Category,
            amount: Double,
            factor: EmissionFactor
        ): ActivityEntry {
            val nowMillis = currentTimeMillis()
            val nowIso = currentTimeIso()

            return ActivityEntry(
                id = "$nowMillis-${category.name}",
                timestampIso = nowIso,
                category = category,
                amount = amount,
                factorId = factor.id,
                co2eTotal = amount * factor.co2ePerUnit,
                explanation = "$amount ${factor.unit} x ${factor.co2ePerUnit} kgCO2e"
            )
        }
    }
}
