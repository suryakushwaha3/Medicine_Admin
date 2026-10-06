package com.example.medicineadmin.model

data class Poster(
    val id: Int,
    val poster_name: String,
    val poster_image: String,
    val is_active: Int,
    val created_at: String
)

data class PosterResponse(
    val success: Boolean,
    val message: String,
    val data: PosterData? = null
)

data class PosterData(
    val id: Int,
    val poster_name: String,
    val poster_image: String
)

data class PosterListResponse(
    val success: Boolean,
    val data: List<Poster> = emptyList()
)

data class SinglePosterResponse(
    val success: Boolean,
    val data: Poster? = null,
    val message: String? = null
)