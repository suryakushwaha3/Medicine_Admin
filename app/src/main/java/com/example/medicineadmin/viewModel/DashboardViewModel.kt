package com.example.medicineadmin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicineadmin.data.Repo.Repository
import com.example.medicineadmin.model.Order
import com.example.medicineadmin.model.Product
import com.example.medicineadmin.model.Sale
import com.example.medicineadmin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = false,

    val users: List<User> = emptyList(),
    val products: List<Product> = emptyList(),
    val orders: List<Order> = emptyList(),
    val sales: List<Sale> = emptyList(),

    val totalUsers: Int = 0,
    val pendingUsers: Int = 0,
    val approvedUsers: Int = 0,
    val blockedUsers: Int = 0,

    val totalProducts: Int = 0,

    val pendingOrders: Int = 0,
    val approvedOrders: Int = 0,
    val completedOrders: Int = 0,

    val totalSales: Double = 0.0,

    val lowStock: Int = 0,

    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DashboardUiState()
    )

    val uiState: StateFlow<DashboardUiState> =
        _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    // ============================================================
    // LOAD DASHBOARD
    // ============================================================

    fun loadDashboard() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {

                // ====================================================
                // CALL ALL APIs IN PARALLEL
                // ====================================================

                val usersDeferred = async {
                    repository.getAllUsers()
                }

                val productsDeferred = async {
                    repository.getProducts()
                }

                val ordersDeferred = async {
                    repository.getOrders()
                }

                val salesDeferred = async {
                    repository.getSellHistory()
                }

                val responses = awaitAll(
                    usersDeferred,
                    productsDeferred,
                    ordersDeferred,
                    salesDeferred
                )

                val usersResponse =
                    responses[0] as retrofit2.Response<List<User>>

                val productsResponse =
                    responses[1] as retrofit2.Response<List<Product>>

                val ordersResponse =
                    responses[2] as retrofit2.Response<List<Order>>

                val salesResponse =
                    responses[3] as retrofit2.Response<List<Sale>>

                // ====================================================
                // CHECK API RESPONSES
                // ====================================================

                if (!usersResponse.isSuccessful) {

                    throw Exception(
                        "Users API failed. Code: ${usersResponse.code()}"
                    )
                }

                if (!productsResponse.isSuccessful) {

                    throw Exception(
                        "Products API failed. Code: ${productsResponse.code()}"
                    )
                }

                if (!ordersResponse.isSuccessful) {

                    throw Exception(
                        "Orders API failed. Code: ${ordersResponse.code()}"
                    )
                }

                if (!salesResponse.isSuccessful) {

                    throw Exception(
                        "Sales API failed. Code: ${salesResponse.code()}"
                    )
                }

                // ====================================================
                // GET DATA
                // ====================================================

                val users =
                    usersResponse.body().orEmpty()

                val products =
                    productsResponse.body().orEmpty()

                val orders =
                    ordersResponse.body().orEmpty()

                val sales =
                    salesResponse.body().orEmpty()

                // ====================================================
                // USER STATISTICS
                // ====================================================

                val totalUsers =
                    users.size

                val pendingUsers =
                    users.count {
                        it.isApproved == 0 &&
                                it.block == 0
                    }

                val approvedUsers =
                    users.count {
                        it.isApproved == 1 &&
                                it.block == 0
                    }

                val blockedUsers =
                    users.count {
                        it.block == 1
                    }

                // ====================================================
                // PRODUCT STATISTICS
                // ====================================================

                val totalProducts =
                    products.size

                val lowStock =
                    products.count {
                        it.stock <= 10
                    }

                // ====================================================
                // ORDER STATISTICS
                // ====================================================

                val pendingOrders =
                    orders.count {
                        it.isApproved == 0
                    }

                val approvedOrders =
                    orders.count {
                        it.isApproved == 1
                    }

                /*
                 * Backend me completed order ka separate
                 * status field nahi hai.
                 *
                 * Isliye currently approved orders ko
                 * completed orders ke roop me count nahi kar rahe.
                 *
                 * Agar backend me completed status add hota hai,
                 * to yahan exact condition add ki ja sakti hai.
                 */

                val completedOrders =
                    0

                // ====================================================
                // SALES STATISTICS
                // ====================================================

                val totalSales =
                    sales.sumOf {
                        it.total_amount
                    }

                // ====================================================
                // UPDATE STATE
                // ====================================================

                _uiState.value = DashboardUiState(

                    isLoading = false,

                    users = users,
                    products = products,
                    orders = orders,
                    sales = sales,

                    totalUsers = totalUsers,
                    pendingUsers = pendingUsers,
                    approvedUsers = approvedUsers,
                    blockedUsers = blockedUsers,

                    totalProducts = totalProducts,

                    pendingOrders = pendingOrders,
                    approvedOrders = approvedOrders,
                    completedOrders = completedOrders,

                    totalSales = totalSales,

                    lowStock = lowStock,

                    error = null
                )

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(

                    isLoading = false,

                    error =
                        e.message
                            ?: "Something went wrong while loading dashboard."
                )
            }
        }
    }

    // ============================================================
    // REFRESH DASHBOARD
    // ============================================================

    fun refreshDashboard() {
        loadDashboard()
    }

    // ============================================================
    // CLEAR ERROR
    // ============================================================

    fun clearError() {

        _uiState.value =
            _uiState.value.copy(
                error = null
            )
    }
}