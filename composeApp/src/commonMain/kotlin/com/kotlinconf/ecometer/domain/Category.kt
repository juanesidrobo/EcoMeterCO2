package com.kotlinconf.ecometer.domain

import kotlinx.serialization.Serializable

@Serializable
enum class Category {
    TRANSPORT,
    FOOD,
    ENERGY,
    PURCHASES
}
