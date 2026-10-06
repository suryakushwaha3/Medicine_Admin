package com.example.medicineadmin.data.Repo

import com.example.medicineadmin.data.ApiService
import com.example.medicineadmin.data.CommonResponse


import com.example.medicineadmin.model.Poster
import com.example.medicineadmin.model.PosterResponse
import com.example.medicineadmin.model.SinglePosterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PosterRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun addPoster(
        posterName: RequestBody,
        posterImage: MultipartBody.Part
    ): Result<PosterResponse> {
        return try {
            val response = apiService.addPoster(
                posterName = posterName,
                posterImage = posterImage
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Failed to add poster: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllPosters(): Result<List<Poster>> {
        return try {
            val response = apiService.getAllPosters()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(
                    Exception("Failed to get posters: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSpecificPoster(
        posterId: Int
    ): Result<SinglePosterResponse> {
        return try {
            val response = apiService.getSpecificPoster(posterId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Poster not found: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePoster(
        posterId: Int,
        posterName: RequestBody,
        posterImage: MultipartBody.Part? = null
    ): Result<CommonResponse> {
        return try {
            val response = apiService.updatePoster(
                posterId = posterId,
                posterName = posterName,
                posterImage = posterImage
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Failed to update poster: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePoster(
        posterId: Int
    ): Result<CommonResponse> {
        return try {
            val response = apiService.deletePoster(posterId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Failed to delete poster: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}