package com.example.medicineadmin.model

data class User(
    val id: Int = 0,
    val user_id: String = "",
    val password: String = "",
    val date_of_account_creation: String = "",
    val isApproved: Int = 0,
    val block: Int = 0,
    val name: String = "",
    val address: String = "",
    val email: String = "",
    val phone_number: String = "",
    val pincode: String = ""
)