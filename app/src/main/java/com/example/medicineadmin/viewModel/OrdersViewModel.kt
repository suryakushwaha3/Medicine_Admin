package com.example.medicineadmin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicineadmin.data.Repo.Repository
import com.example.medicineadmin.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrdersUiState(
    val isLoading: Boolean = false,
    val isAdding: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val orders: List<Order> = emptyList(),
    val selectedOrder: Order? = null,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        OrdersUiState()
    )

    val uiState: StateFlow<OrdersUiState> =
        _uiState.asStateFlow()

    init {
        getOrders()
    }

    // =========================
    // GET ALL ORDERS
    // =========================
    fun getOrders() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {

                val response = repository.getOrders()

                if (response.isSuccessful) {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        orders = response.body().orEmpty(),
                        error = null
                    )

                } else {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to load orders. Code: ${response.code()}"
                    )
                }

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                        ?: "Something went wrong while loading orders."
                )
            }
        }
    }

    // =========================
    // REFRESH ORDERS
    // =========================
    fun refreshOrders() {
        getOrders()
    }

    // =========================
    // GET ORDERS BY USER
    // =========================
    fun getUserOrders(userId: String) {

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
                    repository.getUserOrders(userId)

                if (response.isSuccessful) {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        orders = response.body().orEmpty(),
                        error = null
                    )

                } else {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to load user orders. Code: ${response.code()}"
                    )
                }

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                        ?: "Something went wrong while loading user orders."
                )
            }
        }
    }

    // =========================
    // ADD ORDER
    // =========================
    fun addOrder(
        orderId: String,
        userId: String,
        productId: String,
        isApproved: Int,
        quantity: Int,
        dateOfOrderCreation: String,
        price: Double,
        totalAmount: Double,
        productName: String,
        userName: String,
        message: String,
        category: String
    ) {

        if (orderId.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "Order ID cannot be empty."
            )
            return
        }

        if (userId.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "User ID cannot be empty."
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

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isAdding = true,
                error = null,
                successMessage = null
            )

            try {

                val response = repository.addOrder(
                    orderId = orderId,
                    userId = userId,
                    productId = productId,
                    isApproved = isApproved,
                    quantity = quantity,
                    dateOfOrderCreation = dateOfOrderCreation,
                    price = price,
                    totalAmount = totalAmount,
                    productName = productName,
                    userName = userName,
                    message = message,
                    category = category
                )

                if (response.isSuccessful) {

                    _uiState.value = _uiState.value.copy(
                        isAdding = false,
                        successMessage =
                            response.body()?.message
                                ?: "Order added successfully.",
                        error = null
                    )

                    getOrders()

                } else {

                    _uiState.value = _uiState.value.copy(
                        isAdding = false,
                        error =
                            response.body()?.error
                                ?: "Failed to add order. Code: ${response.code()}",
                        successMessage = null
                    )
                }

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isAdding = false,
                    error = e.message
                        ?: "Something went wrong while adding order.",
                    successMessage = null
                )
            }
        }
    }

    // =========================
    // UPDATE ORDER
    // =========================
    fun updateOrder(
        orderId: String,
        isApproved: Int,
        quantity: Int,
        message: String
    ) {

        if (orderId.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "Order ID cannot be empty."
            )
            return
        }

        if (quantity <= 0) {
            _uiState.value = _uiState.value.copy(
                error = "Quantity must be greater than 0."
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isUpdating = true,
                error = null,
                successMessage = null
            )

            try {

                val response = repository.updateOrder(
                    orderId = orderId,
                    isApproved = isApproved,
                    quantity = quantity,
                    message = message
                )

                if (response.isSuccessful) {

                    _uiState.value = _uiState.value.copy(
                        isUpdating = false,
                        successMessage =
                            response.body()?.message
                                ?: "Order updated successfully.",
                        error = null
                    )

                    getOrders()

                } else {

                    _uiState.value = _uiState.value.copy(
                        isUpdating = false,
                        error =
                            response.body()?.error
                                ?: "Failed to update order. Code: ${response.code()}",
                        successMessage = null
                    )
                }

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    error = e.message
                        ?: "Something went wrong while updating order.",
                    successMessage = null
                )
            }
        }
    }

    // =========================
    // DELETE ORDER
    // =========================
    fun deleteOrder(
        orderId: String
    ) {

        if (orderId.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "Order ID cannot be empty."
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
                    repository.deleteOrder(orderId)

                if (response.isSuccessful) {

                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        successMessage =
                            response.body()?.message
                                ?: "Order deleted successfully.",
                        error = null
                    )

                    getOrders()

                } else {

                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        error =
                            response.body()?.error
                                ?: "Failed to delete order. Code: ${response.code()}",
                        successMessage = null
                    )
                }

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    error = e.message
                        ?: "Something went wrong while deleting order.",
                    successMessage = null
                )
            }
        }
    }

    // =========================
    // SELECT ORDER
    // =========================
    fun selectOrder(order: Order) {
        _uiState.value = _uiState.value.copy(
            selectedOrder = order
        )
    }

    // =========================
    // CLEAR SELECTED ORDER
    // =========================
    fun clearSelectedOrder() {
        _uiState.value = _uiState.value.copy(
            selectedOrder = null
        )
    }

    // =========================
    // CLEAR ERROR
    // =========================
    fun clearError() {
        _uiState.value = _uiState.value.copy(
            error = null
        )
    }

    // =========================
    // CLEAR SUCCESS MESSAGE
    // =========================
    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(
            successMessage = null
        )
    }
}