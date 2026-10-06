package com.example.medicineadmin.model

data class AvailableStock(
    val id: Int = 0,
    val product_id: String = "",
    val product_name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val user_id: String = "",
    val user_name: String = ""
)