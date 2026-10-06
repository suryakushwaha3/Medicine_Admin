package com.example.medicineadmin.model

data class Order(
    val id: Int = 0,
    val order_id: String = "",
    val user_id: String = "",
    val product_id: String = "",
    val isApproved: Int = 0,
    val quantity: Int = 0,
    val date_of_order_creation: String = "",
    val price: Double = 0.0,
    val total_amount: Double = 0.0,
    val product_name: String = "",
    val user_name: String = "",
    val message: String = "",
    val category: String = ""
)