package com.example.medicineadmin.data.Repo


 import com.example.medicineadmin.data.ApiService
 import com.example.medicineadmin.data.CommonResponse
 import com.example.medicineadmin.model.Category
import com.example.medicineadmin.model.CategoryResponse
 import com.example.medicineadmin.model.SingleCategoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun addCategory(
        categoryName: RequestBody,
        categoryImage: MultipartBody.Part
    ): Result<CategoryResponse> {
        return try {
            val response = apiService.addCategory(
                categoryName = categoryName,
                categoryImage = categoryImage
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Failed to add category: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllCategories(): Result<List<Category>> {
        return try {
            val response = apiService.getAllCategories()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(
                    Exception("Failed to get categories: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSpecificCategory(
        categoryId: Int
    ): Result<SingleCategoryResponse> {
        return try {
            val response = apiService.getSpecificCategory(categoryId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Category not found: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCategory(
        categoryId: Int,
        categoryName: RequestBody,
        categoryImage: MultipartBody.Part? = null
    ): Result<CommonResponse> {
        return try {
            val response = apiService.updateCategory(
                categoryId = categoryId,
                categoryName = categoryName,
                categoryImage = categoryImage
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Failed to update category: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCategory(
        categoryId: Int
    ): Result<CommonResponse> {
        return try {
            val response = apiService.deleteCategory(categoryId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Failed to delete category: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}