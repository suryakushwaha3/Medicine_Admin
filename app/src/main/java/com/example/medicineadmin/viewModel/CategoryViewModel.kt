package com.example.medicineadmin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicineadmin.data.Repo.CategoryRepository
import com.example.medicineadmin.model.Category
 import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

data class CategoryUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val message: String? = null
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        getAllCategories()
    }

    fun getAllCategories() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            repository.getAllCategories()
                .onSuccess { categories ->

                    _uiState.value = _uiState.value.copy(
                        categories = categories,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { exception ->

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message
                            ?: "Failed to load categories."
                    )
                }
        }
    }

    fun addCategory(
        categoryName: RequestBody,
        categoryImage: MultipartBody.Part
    ) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isSaving = true,
                error = null,
                message = null
            )

            repository.addCategory(
                categoryName = categoryName,
                categoryImage = categoryImage
            )
                .onSuccess { response ->

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        message = response.message,
                        error = null
                    )

                    getAllCategories()
                }
                .onFailure { exception ->

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = exception.message
                            ?: "Failed to add category."
                    )
                }
        }
    }

    fun updateCategory(
        categoryId: Int,
        categoryName: RequestBody,
        categoryImage: MultipartBody.Part? = null
    ) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isSaving = true,
                error = null,
                message = null
            )

            repository.updateCategory(
                categoryId = categoryId,
                categoryName = categoryName,
                categoryImage = categoryImage
            )
                .onSuccess { response ->

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        message = response.message,
                        error = null
                    )

                    getAllCategories()
                }
                .onFailure { exception ->

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = exception.message
                            ?: "Failed to update category."
                    )
                }
        }
    }

    fun deleteCategory(categoryId: Int) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isSaving = true,
                error = null,
                message = null
            )

            repository.deleteCategory(categoryId)
                .onSuccess { response ->

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        message = response.message,
                        error = null
                    )

                    getAllCategories()
                }
                .onFailure { exception ->

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = exception.message
                            ?: "Failed to delete category."
                    )
                }
        }
    }

    fun refreshCategories() {
        getAllCategories()
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(
            message = null
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            error = null
        )
    }
}