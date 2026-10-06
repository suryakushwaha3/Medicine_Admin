package com.example.medicineadmin.model

data class Product(
    val id: Int = 0,
    val product_id: String = "",
    val product_name: String = "",
    val category: String = "",
    val stock: Int = 0,
    val product_image: String? = null
)