package com.example.medicineadmin.data

data class ApiResponse(
    val data: Any? = null,
    val success: Boolean = false,
    val message: String? = null,
    val error: String? = null
)