package com.example.medicineadmin.model


data class Notification(
    val id: Int = 0,
    val title: String = "",
    val message: String = "",
    val type: String = "",
    val is_read: Int = 0,
    val created_at: String = ""
)