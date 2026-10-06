package com.example.medicineadmin.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicineadmin.data.Repo.Repository
import com.example.medicineadmin.model.Sale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SalesUiState(
    val isLoading: Boolean = false,
    val isAdding: Boolean = false,
    val isDeleting: Boolean = false,
    val sales: List<Sale> = emptyList(),
    val selectedSale: Sale? = null,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SalesUiState()
    )

    val uiState: StateFlow<SalesUiState> =
        _uiState.asStateFlow()

    init {
        getSellHistory()
    }

     // GET ALL SELL HISTORY


    fun getSellHistory() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {

                val response = repository.getSellHistory()

                if (response.isSuccessful) {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        sales = response.body().orEmpty(),
                        error = null
                    )

                } else {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to load sales. Code: ${response.code()}"
                    )
                }

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                        ?: "Something went wrong while loading sales."
                )
            }
        }
    }


    // REFRESH SELL HISTORY


    fun refreshSales() {
        getSellHistory()
    }

     // GET USER SELL HISTORY

    fun getUserSellHistory(
        userId: String
    ) {

        if (userId.isBlank()) {

            _uiState.value = _uiState.value.copy(
                error = "User ID cannot be empty."
            )

            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {

                val response =
                    repository.getUserSellHistory(userId)

                if (response.isSuccessful) {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        sales = response.body().orEmpty(),
                        error = null
                    )

                } else {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error =
                            "Failed to load user sales. Code: ${response.code()}"
                    )
                }

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                        ?: "Something went wrong while loading user sales."
                )
            }
        }
    }


    // ADD SELL

    fun addSell(
        sellId: String,
        productId: String,
        quantity: Int,
        reaminingStock: Int,
        dateOfSell: String,
        totalAmount: Double,
        price: Double,
        productName: String,
        userName: String,
        userId: String
    ) {

        if (sellId.isBlank()) {

            _uiState.value = _uiState.value.copy(
                error = "Sell ID cannot be empty."
            )

            return
        }

        if (productId.isBlank()) {

            _uiState.value = _uiState.value.copy(
                error = "Product ID cannot be empty."
            )

            return
        }

        if (quantity <= 0) {

            _uiState.value = _uiState.value.copy(
                error = "Quantity must be greater than 0."
            )

            return
        }

        if (reaminingStock < 0) {

            _uiState.value = _uiState.value.copy(
                error = "Remaining stock cannot be negative."
            )

            return
        }

        if (price < 0) {

            _uiState.value = _uiState.value.copy(
                error = "Price cannot be negative."
            )

            return
        }

        if (totalAmount < 0) {

            _uiState.value = _uiState.value.copy(
                error = "Total amount cannot be negative."
            )

            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isAdding = true,
                error = null,
                successMessage = null
            )

            try {

                val response = repository.addSell(
                    sellId = sellId,
                    productId = productId,
                    quantity = quantity,
                    reaminingStock = reaminingStock,
                    dateOfSell = dateOfSell,
                    totalAmount = totalAmount,
                    price = price,
                    productName = productName,
                    userName = userName,
                    userId = userId
                )

                if (response.isSuccessful) {

                    _uiState.value = _uiState.value.copy(
                        isAdding = false,
                        successMessage =
                            response.body()?.message
                                ?: "Sell added successfully.",
                        error = null
                    )

                    getSellHistory()

                } else {

                    _uiState.value = _uiState.value.copy(
                        isAdding = false,
                        error =
                            response.body()?.error
                                ?: "Failed to add sell. Code: ${response.code()}",
                        successMessage = null
                    )
                }

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isAdding = false,
                    error = e.message
                        ?: "Something went wrong while adding sell.",
                    successMessage = null
                )
            }
        }
    }

     // DELETE SELL

    fun deleteSell(
        sellId: String
    ) {

        if (sellId.isBlank()) {

            _uiState.value = _uiState.value.copy(
                error = "Sell ID cannot be empty."
            )

            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isDeleting = true,
                error = null,
                successMessage = null
            )

            try {

                val response =
                    repository.deleteSell(sellId)

                if (response.isSuccessful) {

                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        successMessage =
                            response.body()?.message
                                ?: "Sell deleted successfully.",
                        error = null
                    )

                    getSellHistory()

                } else {

                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        error =
                            response.body()?.error
                                ?: "Failed to delete sell. Code: ${response.code()}",
                        successMessage = null
                    )
                }

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    error = e.message
                        ?: "Something went wrong while deleting sell.",
                    successMessage = null
                )
            }
        }
    }

     // SELECT SALE


    fun selectSale(
        sale: Sale
    ) {

        _uiState.value = _uiState.value.copy(
            selectedSale = sale
        )
    }

     // CLEAR SELECTED SALE

    fun clearSelectedSale() {

        _uiState.value = _uiState.value.copy(
            selectedSale = null
        )
    }

     // CLEAR ERROR

    fun clearError() {

        _uiState.value = _uiState.value.copy(
            error = null
        )
    }


    // CLEAR SUCCESS MESSAGE

    fun clearSuccessMessage() {

        _uiState.value = _uiState.value.copy(
            successMessage = null
        )
    }
}