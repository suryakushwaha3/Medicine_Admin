package com.example.medicineadmin.model

data class Category(
    val id: Int,
    val category_name: String,
    val category_image: String,
    val is_active: Int,
    val created_at: String
)

data class CategoryResponse(
    val success: Boolean,
    val message: String? = null,
    val data: CategoryData? = null
)

data class CategoryData(
    val id: Int,
    val category_name: String,
    val category_image: String
)

data class CategoryListResponse(
    val success: Boolean,
    val data: List<Category> = emptyList(),
    val message: String? = null
)

data class SingleCategoryResponse(
    val success: Boolean,
    val data: Category? = null,
    val message: String? = null
)