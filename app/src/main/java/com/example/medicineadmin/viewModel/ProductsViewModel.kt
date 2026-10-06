package com.example.medicineadmin.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicineadmin.data.Repo.Repository
import com.example.medicineadmin.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductsUiState(
    val isLoading: Boolean = false,
    val isAdding: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val products: List<Product> = emptyList(),
    val selectedProduct: Product? = null,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val response = repository.getProducts()

                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        products = response.body().orEmpty(),
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to load products. Code: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                        ?: "Something went wrong while loading products."
                )
            }
        }
    }

    fun refreshProducts() {
        getProducts()
    }

    fun addProduct(
        productId: String,
        productName: String,
        category: String,
        stock: Int,
        imageUri: Uri?
    ) {
        if (productId.isBlank()) {
            setError("Product ID cannot be empty.")
            return
        }

        if (productName.isBlank()) {
            setError("Product name cannot be empty.")
            return
        }

        if (category.isBlank()) {
            setError("Category cannot be empty.")
            return
        }

        if (stock < 0) {
            setError("Stock cannot be negative.")
            return
        }

        if (imageUri == null) {
            setError("Please select a product image.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isAdding = true,
                error = null,
                successMessage = null
            )

            try {
                val response = repository.addProduct(
                    productId = productId.trim(),
                    productName = productName.trim(),
                    category = category.trim(),
                    stock = stock,
                    imageUri = imageUri
                )

                Log.d(
                    "ADD_PRODUCT",
                    "Code=${response.code()}, Body=${response.body()}, Error=${response.errorBody()?.string()}"
                )

                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        isAdding = false,
                        successMessage =
                            response.body()?.message
                                ?: "Product added successfully.",
                        error = null
                    )
                    getProducts()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isAdding = false,
                        error =
                            response.body()?.error
                                ?: "Failed to add product. Code: ${response.code()}",
                        successMessage = null
                    )
                }
            } catch (e: Exception) {
                Log.e("ADD_PRODUCT", "Add product failed", e)

                _uiState.value = _uiState.value.copy(
                    isAdding = false,
                    error = e.message
                        ?: "Something went wrong while adding product.",
                    successMessage = null
                )
            }
        }
    }

    fun updateProduct(
        productId: String,
        productName: String,
        category: String,
        stock: Int,
        imageUri: Uri? = null
    ) {
        if (productId.isBlank()) {
            setError("Product ID cannot be empty.")
            return
        }

        if (productName.isBlank()) {
            setError("Product name cannot be empty.")
            return
        }

        if (category.isBlank()) {
            setError("Category cannot be empty.")
            return
        }

        if (stock < 0) {
            setError("Stock cannot be negative.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isUpdating = true,
                error = null,
                successMessage = null
            )

            try {
                val response = repository.updateProduct(
                    productId = productId.trim(),
                    productName = productName.trim(),
                    category = category.trim(),
                    stock = stock,
                    imageUri = imageUri
                )

                Log.d(
                    "UPDATE_PRODUCT",
                    "Code=${response.code()}, Message=${response.message()}, Body=${response.body()}, Error=${response.errorBody()?.string()}"
                )

                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        isUpdating = false,
                        successMessage =
                            response.body()?.message
                                ?: "Product updated successfully.",
                        error = null
                    )

                    getProducts()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isUpdating = false,
                        error =
                            response.body()?.error
                                ?: "Failed to update product. Code: ${response.code()}",
                        successMessage = null
                    )
                }
            } catch (e: Exception) {
                Log.e(
                    "UPDATE_PRODUCT",
                    "Update product failed",
                    e
                )

                _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    error = e.message
                        ?: "Something went wrong while updating product.",
                    successMessage = null
                )
            }
        }
    }

    fun deleteProduct(productId: String) {
        if (productId.isBlank()) {
            setError("Product ID cannot be empty.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeleting = true,
                error = null,
                successMessage = null
            )

            try {
                val response = repository.deleteProduct(productId.trim())

                Log.d(
                    "DELETE_PRODUCT",
                    "Code=${response.code()}, Message=${response.message()}, Body=${response.body()}, Error=${response.errorBody()?.string()}"
                )

                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        successMessage =
                            response.body()?.message
                                ?: "Product deleted successfully.",
                        error = null
                    )

                    getProducts()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        error =
                            response.body()?.error
                                ?: "Failed to delete product. Code: ${response.code()}",
                        successMessage = null
                    )
                }
            } catch (e: Exception) {
                Log.e(
                    "DELETE_PRODUCT",
                    "Delete product failed",
                    e
                )

                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    error = e.message
                        ?: "Something went wrong while deleting product.",
                    successMessage = null
                )
            }
        }
    }

    fun selectProduct(product: Product) {
        _uiState.value = _uiState.value.copy(
            selectedProduct = product
        )
    }

    fun clearSelectedProduct() {
        _uiState.value = _uiState.value.copy(
            selectedProduct = null
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            error = null
        )
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(
            successMessage = null
        )
    }

    private fun setError(message: String) {
        _uiState.value = _uiState.value.copy(
            error = message,
            successMessage = null
        )
    }
}