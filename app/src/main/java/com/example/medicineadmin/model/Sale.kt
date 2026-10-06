package com.example.medicineadmin.model

data class Sale(
    val id: Int = 0,
    val sell_id: String = "",
    val product_id: String = "",
    val quantity: Int = 0,
    val reamining_stock: Int = 0,
    val date_of_sell: String = "",
    val total_amount: Double = 0.0,
    val price: Double = 0.0,
    val product_name: String = "",
    val user_name: String = "",
    val user_id: String = "",
    val product_image: String? = null
)