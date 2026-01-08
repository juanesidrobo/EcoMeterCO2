package com.kotlinconf.ecometer.data

import com.kotlinconf.ecometer.domain.Category
import com.kotlinconf.ecometer.domain.EmissionFactor

// Fallback data when JSON loading fails
val HARDCODED_FACTORS = listOf(
    // === TRANSPORT ===
    EmissionFactor(
        id = "transport_car_petrol",
        category = Category.TRANSPORT,
        name = "Car (Petrol)",
        unit = "km",
        co2ePerUnit = 0.170,
        source = "UK Gov GHG 2025"
    ),
    EmissionFactor(
        id = "transport_car_diesel",
        category = Category.TRANSPORT,
        name = "Car (Diesel)",
        unit = "km",
        co2ePerUnit = 0.158,
        source = "UK Gov GHG 2025"
    ),
    EmissionFactor(
        id = "transport_car_electric",
        category = Category.TRANSPORT,
        name = "Electric Car",
        unit = "km",
        co2ePerUnit = 0.047,
        source = "UK Gov GHG 2025"
    ),
    EmissionFactor(
        id = "transport_car_hybrid",
        category = Category.TRANSPORT,
        name = "Hybrid Car",
        unit = "km",
        co2ePerUnit = 0.108,
        source = "UK Gov GHG 2025"
    ),
    EmissionFactor(
        id = "transport_motorcycle",
        category = Category.TRANSPORT,
        name = "Motorcycle",
        unit = "km",
        co2ePerUnit = 0.103,
        source = "UK Gov GHG 2025"
    ),
    EmissionFactor(
        id = "transport_bus",
        category = Category.TRANSPORT,
        name = "Public Bus",
        unit = "km",
        co2ePerUnit = 0.096,
        source = "UK Gov GHG 2025"
    ),
    EmissionFactor(
        id = "transport_metro",
        category = Category.TRANSPORT,
        name = "Metro/Subway",
        unit = "km",
        co2ePerUnit = 0.035,
        source = "UK Gov GHG 2025"
    ),
    EmissionFactor(
        id = "transport_train",
        category = Category.TRANSPORT,
        name = "Intercity Train",
        unit = "km",
        co2ePerUnit = 0.041,
        source = "UK Gov GHG 2025"
    ),
    EmissionFactor(
        id = "transport_flight_short",
        category = Category.TRANSPORT,
        name = "Short Flight (<3h)",
        unit = "km",
        co2ePerUnit = 0.255,
        source = "UK Gov GHG 2025"
    ),
    EmissionFactor(
        id = "transport_flight_long",
        category = Category.TRANSPORT,
        name = "Long Flight (>3h)",
        unit = "km",
        co2ePerUnit = 0.195,
        source = "UK Gov GHG 2025"
    ),
    EmissionFactor(
        id = "transport_taxi",
        category = Category.TRANSPORT,
        name = "Taxi/Uber",
        unit = "km",
        co2ePerUnit = 0.203,
        source = "UK Gov GHG 2025"
    ),
    EmissionFactor(
        id = "transport_bike",
        category = Category.TRANSPORT,
        name = "Bicycle",
        unit = "km",
        co2ePerUnit = 0.0,
        source = "Zero emissions"
    ),
    EmissionFactor(
        id = "transport_walk",
        category = Category.TRANSPORT,
        name = "Walking",
        unit = "km",
        co2ePerUnit = 0.0,
        source = "Zero emissions"
    ),

    // === FOOD ===
    EmissionFactor(
        id = "food_meal_beef",
        category = Category.FOOD,
        name = "Beef Meal",
        unit = "serving",
        co2ePerUnit = 6.61,
        source = "Poore & Nemecek 2018"
    ),
    EmissionFactor(
        id = "food_meal_lamb",
        category = Category.FOOD,
        name = "Lamb Meal",
        unit = "serving",
        co2ePerUnit = 5.84,
        source = "Poore & Nemecek 2018"
    ),
    EmissionFactor(
        id = "food_meal_pork",
        category = Category.FOOD,
        name = "Pork Meal",
        unit = "serving",
        co2ePerUnit = 2.41,
        source = "Poore & Nemecek 2018"
    ),
    EmissionFactor(
        id = "food_meal_chicken",
        category = Category.FOOD,
        name = "Chicken Meal",
        unit = "serving",
        co2ePerUnit = 1.82,
        source = "Poore & Nemecek 2018"
    ),
    EmissionFactor(
        id = "food_meal_fish",
        category = Category.FOOD,
        name = "Fish Meal",
        unit = "serving",
        co2ePerUnit = 1.34,
        source = "Poore & Nemecek 2018"
    ),
    EmissionFactor(
        id = "food_meal_eggs",
        category = Category.FOOD,
        name = "Egg Meal",
        unit = "serving",
        co2ePerUnit = 0.98,
        source = "Poore & Nemecek 2018"
    ),
    EmissionFactor(
        id = "food_meal_vegetarian",
        category = Category.FOOD,
        name = "Vegetarian Meal",
        unit = "serving",
        co2ePerUnit = 0.51,
        source = "Poore & Nemecek 2018"
    ),
    EmissionFactor(
        id = "food_meal_vegan",
        category = Category.FOOD,
        name = "Vegan Meal",
        unit = "serving",
        co2ePerUnit = 0.35,
        source = "Poore & Nemecek 2018"
    ),
    EmissionFactor(
        id = "food_coffee",
        category = Category.FOOD,
        name = "Coffee",
        unit = "cup",
        co2ePerUnit = 0.21,
        source = "Carbon Trust"
    ),
    EmissionFactor(
        id = "food_milk_dairy",
        category = Category.FOOD,
        name = "Dairy Milk",
        unit = "liter",
        co2ePerUnit = 1.39,
        source = "Poore & Nemecek 2018"
    ),
    EmissionFactor(
        id = "food_milk_oat",
        category = Category.FOOD,
        name = "Oat Milk",
        unit = "liter",
        co2ePerUnit = 0.29,
        source = "Poore & Nemecek 2018"
    ),
    EmissionFactor(
        id = "food_cheese",
        category = Category.FOOD,
        name = "Cheese",
        unit = "kg",
        co2ePerUnit = 13.5,
        source = "Poore & Nemecek 2018"
    ),

    // === ENERGY ===
    EmissionFactor(
        id = "energy_electricity",
        category = Category.ENERGY,
        name = "Electricity (Average)",
        unit = "kWh",
        co2ePerUnit = 0.41,
        source = "Global Avg 2024"
    ),
    EmissionFactor(
        id = "energy_electricity_renewable",
        category = Category.ENERGY,
        name = "Renewable Electricity",
        unit = "kWh",
        co2ePerUnit = 0.02,
        source = "IPCC"
    ),
    EmissionFactor(
        id = "energy_natural_gas",
        category = Category.ENERGY,
        name = "Natural Gas (Heating)",
        unit = "kWh",
        co2ePerUnit = 0.18,
        source = "UK Gov GHG 2025"
    ),
    EmissionFactor(
        id = "energy_lpg",
        category = Category.ENERGY,
        name = "LPG (Cooking)",
        unit = "kg",
        co2ePerUnit = 2.98,
        source = "UK Gov GHG 2025"
    ),
    EmissionFactor(
        id = "energy_heating_oil",
        category = Category.ENERGY,
        name = "Heating Oil",
        unit = "liter",
        co2ePerUnit = 2.52,
        source = "UK Gov GHG 2025"
    ),
    EmissionFactor(
        id = "energy_water_hot",
        category = Category.ENERGY,
        name = "Hot Water",
        unit = "liter",
        co2ePerUnit = 0.05,
        source = "Estimate"
    ),
    EmissionFactor(
        id = "energy_ac",
        category = Category.ENERGY,
        name = "Air Conditioning",
        unit = "hour",
        co2ePerUnit = 0.9,
        source = "EPA Estimate"
    ),

    // === PURCHASES ===
    EmissionFactor(
        id = "purchase_clothing_new",
        category = Category.PURCHASES,
        name = "New Clothing",
        unit = "item",
        co2ePerUnit = 8.5,
        source = "Carbon Trust"
    ),
    EmissionFactor(
        id = "purchase_clothing_secondhand",
        category = Category.PURCHASES,
        name = "Secondhand Clothing",
        unit = "item",
        co2ePerUnit = 0.5,
        source = "Estimate"
    ),
    EmissionFactor(
        id = "purchase_smartphone",
        category = Category.PURCHASES,
        name = "Smartphone",
        unit = "unit",
        co2ePerUnit = 70.0,
        source = "Apple Environmental Report"
    ),
    EmissionFactor(
        id = "purchase_laptop",
        category = Category.PURCHASES,
        name = "Laptop",
        unit = "unit",
        co2ePerUnit = 300.0,
        source = "Dell Environmental Report"
    ),
    EmissionFactor(
        id = "purchase_tv",
        category = Category.PURCHASES,
        name = "Television",
        unit = "unit",
        co2ePerUnit = 200.0,
        source = "Industry Estimate"
    ),
    EmissionFactor(
        id = "purchase_furniture",
        category = Category.PURCHASES,
        name = "Medium Furniture",
        unit = "unit",
        co2ePerUnit = 50.0,
        source = "Industry Estimate"
    ),
    EmissionFactor(
        id = "purchase_book",
        category = Category.PURCHASES,
        name = "New Book",
        unit = "unit",
        co2ePerUnit = 1.1,
        source = "Carbon Trust"
    ),
    EmissionFactor(
        id = "purchase_shoes",
        category = Category.PURCHASES,
        name = "Shoes",
        unit = "pair",
        co2ePerUnit = 14.0,
        source = "MIT Study"
    ),
    EmissionFactor(
        id = "purchase_plastic_bag",
        category = Category.PURCHASES,
        name = "Plastic Bag",
        unit = "unit",
        co2ePerUnit = 0.033,
        source = "UK Environment Agency"
    ),
    EmissionFactor(
        id = "purchase_reusable_bag",
        category = Category.PURCHASES,
        name = "Reusable Bag",
        unit = "unit",
        co2ePerUnit = 0.005,
        source = "UK Environment Agency"
    )
)
