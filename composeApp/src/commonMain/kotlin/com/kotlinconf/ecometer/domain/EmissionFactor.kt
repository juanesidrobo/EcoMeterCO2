package com.kotlinconf.ecometer.domain

import kotlinx.serialization.Serializable

@Serializable
data class EmissionFactor(
    val id: String,
    val category: Category,
    val name: String,         // Ex: "Car (Petrol)", "Beef Meal"
    val unit: String,         // Ex: "km", "serving", "kWh"
    val co2ePerUnit: Double,  // Ex: factor magic
    val source: String = "UK Gov GHG 2025"
)
