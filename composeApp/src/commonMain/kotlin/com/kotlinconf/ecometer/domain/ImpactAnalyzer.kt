package com.kotlinconf.ecometer.domain

import com.kotlinconf.ecometer.data.ActivityRepository

/**
 * Regional emission averages for comparison.
 * These would ideally come from an API or database with real regional data.
 */
object RegionalAverages {

    // Average daily CO2e emissions in kg per person by region
    private val dailyAveragesByRegion = mapOf(
        "global" to 13.7,        // Global average ~5 tonnes/year ÷ 365
        "usa" to 43.8,           // USA ~16 tonnes/year
        "eu" to 19.2,            // EU ~7 tonnes/year
        "uk" to 15.1,            // UK ~5.5 tonnes/year
        "china" to 21.1,         // China ~7.7 tonnes/year
        "india" to 5.5,          // India ~2 tonnes/year
        "latam" to 8.2,          // Latin America ~3 tonnes/year
        "africa" to 3.0,         // Africa ~1.1 tonnes/year
        "japan" to 24.7,         // Japan ~9 tonnes/year
        "australia" to 46.0      // Australia ~16.8 tonnes/year
    )

    // Recommended sustainable daily limit (based on Paris Agreement targets)
    const val SUSTAINABLE_DAILY_LIMIT = 5.5 // kg CO2e per day (~2 tonnes/year)

    fun getDailyAverageForRegion(regionCode: String): Double {
        return dailyAveragesByRegion[regionCode.lowercase()] ?: dailyAveragesByRegion["global"]!!
    }

    fun getAvailableRegions(): List<String> {
        return dailyAveragesByRegion.keys.toList()
    }

    /**
     * Compare user's emissions to regional average.
     * Returns a percentage: <100 means below average (good), >100 means above average
     */
    fun compareToRegionalAverage(userDailyEmissions: Double, regionCode: String): Double {
        val regionalAverage = getDailyAverageForRegion(regionCode)
        return if (regionalAverage > 0) {
            (userDailyEmissions / regionalAverage) * 100
        } else {
            0.0
        }
    }

    /**
     * Compare user's emissions to sustainable target.
     * Returns a percentage: <100 means sustainable, >100 means above target
     */
    fun compareToSustainableTarget(userDailyEmissions: Double): Double {
        return (userDailyEmissions / SUSTAINABLE_DAILY_LIMIT) * 100
    }
}

/**
 * Simple AI classifier for activities.
 * In a production app, this would use ML or an API.
 */
object ActivityClassifier {

    private val keywordToCategory = mapOf(
        // Transport
        "car" to Category.TRANSPORT,
        "drive" to Category.TRANSPORT,
        "bus" to Category.TRANSPORT,
        "train" to Category.TRANSPORT,
        "flight" to Category.TRANSPORT,
        "plane" to Category.TRANSPORT,
        "bike" to Category.TRANSPORT,
        "metro" to Category.TRANSPORT,
        "uber" to Category.TRANSPORT,
        "taxi" to Category.TRANSPORT,

        // Food
        "eat" to Category.FOOD,
        "food" to Category.FOOD,
        "meal" to Category.FOOD,
        "beef" to Category.FOOD,
        "chicken" to Category.FOOD,
        "vegetable" to Category.FOOD,
        "vegan" to Category.FOOD,
        "lunch" to Category.FOOD,
        "dinner" to Category.FOOD,
        "breakfast" to Category.FOOD,

        // Energy
        "electricity" to Category.ENERGY,
        "power" to Category.ENERGY,
        "heating" to Category.ENERGY,
        "ac" to Category.ENERGY,
        "air conditioning" to Category.ENERGY,
        "light" to Category.ENERGY,
        "gas" to Category.ENERGY,

        // Purchases
        "buy" to Category.PURCHASES,
        "purchase" to Category.PURCHASES,
        "shop" to Category.PURCHASES,
        "clothes" to Category.PURCHASES,
        "clothing" to Category.PURCHASES,
        "electronics" to Category.PURCHASES
    )

    /**
     * Classify activity description into a category.
     * Returns null if cannot determine category.
     */
    fun classifyActivity(description: String): Category? {
        val lowerDescription = description.lowercase()

        for ((keyword, category) in keywordToCategory) {
            if (lowerDescription.contains(keyword)) {
                return category
            }
        }

        return null
    }

    /**
     * Suggest best matching emission factor based on description.
     */
    fun suggestFactorId(description: String, category: Category): String? {
        val lower = description.lowercase()

        return when (category) {
            Category.TRANSPORT -> when {
                lower.contains("car") || lower.contains("drive") -> "transport_car_petrol"
                lower.contains("bus") -> "transport_bus"
                else -> "transport_car_petrol"
            }
            Category.FOOD -> when {
                lower.contains("beef") -> "food_meal_beef"
                lower.contains("chicken") -> "food_meal_chicken"
                lower.contains("vegan") || lower.contains("vegetable") -> "food_meal_vegan"
                else -> "food_meal_chicken"
            }
            Category.ENERGY -> "energy_electricity"
            Category.PURCHASES -> "purchase_clothing"
        }
    }
}

/**
 * Report generator for weekly/monthly summaries.
 */
class ReportGenerator(private val activityRepository: ActivityRepository) {

    data class Report(
        val periodDays: Int,
        val totalEmissions: Double,
        val emissionsByCategory: Map<Category, Double>,
        val averageDailyEmissions: Double,
        val comparisonToGlobalAverage: Double,
        val comparisonToSustainableTarget: Double,
        val entryCount: Int
    )

    fun generateWeeklyReport(): Report {
        return generateReport(7)
    }

    fun generateMonthlyReport(): Report {
        return generateReport(30)
    }

    fun generateReport(days: Int): Report {
        val totalEmissions = activityRepository.getTotalEmissionsForLastDays(days)
        val emissionsByCategory = activityRepository.getEmissionsByCategoryForLastDays(days)
        val entries = activityRepository.getEntriesFromLastDays(days)

        val averageDaily = if (days > 0) totalEmissions / days else 0.0

        return Report(
            periodDays = days,
            totalEmissions = totalEmissions,
            emissionsByCategory = emissionsByCategory,
            averageDailyEmissions = averageDaily,
            comparisonToGlobalAverage = RegionalAverages.compareToRegionalAverage(averageDaily, "global"),
            comparisonToSustainableTarget = RegionalAverages.compareToSustainableTarget(averageDaily),
            entryCount = entries.size
        )
    }
}

