package com.example.mealmindle.data

data class ProductResponse(
    val product: ProductDetails,
    val status: Int,
    val status_verbose: String
)

data class ProductDetails(
    val product_name: String,
    val quantity: String,
    val ingredients_text: String,
    val nutriments: Nutriments
    // Weitere Felder nach Bedarf...
)

data class Nutriments(
    val carbohydrates_100g: Double,
    val proteins_100g: Double,
    val fat_100g: Double,
    val energy_kcal_100g: Int
    // Weitere Nährstofffelder nach Bedarf...
)
