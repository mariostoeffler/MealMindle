package com.example.mealmindle.data

data class ProductResponse(
    val product: ProductDetails, // <-- Hier sollte die Eigenschaft 'product' definiert sein.
    val status: Int,
    val status_verbose: String

)

data class ProductDetails(
    val product_name: String,
    val quantity: String
)

data class ScannedProduct(
    val productName: String,
    val quantity: String
)
