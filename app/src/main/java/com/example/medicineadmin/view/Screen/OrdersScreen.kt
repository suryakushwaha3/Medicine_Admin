package com.example.medicineadmin.view.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.medicineadmin.model.Order
import com.example.medicineadmin.view.components.AdminBottomBar
import com.example.medicineadmin.view.navigation.Routes
import com.example.medicineadmin.viewModel.OrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    navController: NavController,
    viewModel: OrdersViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    val tabs = listOf(
        "Pending",
        "Approved"
    )

    val filteredOrders = uiState.orders.filter { order ->

        val status = if (order.isApproved == 1) {
            "Approved"
        } else {
            "Pending"
        }

        val matchesTab =
            status == tabs[selectedTab]

        val matchesSearch =
            order.order_id.contains(
                searchQuery,
                ignoreCase = true
            ) ||
                    order.user_name.contains(
                        searchQuery,
                        ignoreCase = true
                    ) ||
                    order.product_name.contains(
                        searchQuery,
                        ignoreCase = true
                    ) ||
                    order.user_id.contains(
                        searchQuery,
                        ignoreCase = true
                    ) ||
                    order.product_id.contains(
                        searchQuery,
                        ignoreCase = true
                    )

        matchesTab && matchesSearch
    }

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Column {

                        Text(
                            text = "Orders",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Manage customer orders",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            )
        },

        bottomBar = {
            AdminBottomBar(
                navController = navController,
                currentRoute = Routes.Orders
            )
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background
                )
                .padding(innerPadding)
        ) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                singleLine = true,
                placeholder = {
                    Text("Search orders...")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                shape = RoundedCornerShape(14.dp)
            )

            TabRow(
                selectedTabIndex = selectedTab
            ) {

                tabs.forEachIndexed { index, title ->

                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                        },
                        text = {
                            Text(title)
                        }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            when {

                uiState.isLoading -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        CircularProgressIndicator()
                    }
                }

                uiState.error != null &&
                        uiState.orders.isEmpty() -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.error
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = uiState.error
                                ?: "Something went wrong",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        TextButton(
                            onClick = {
                                viewModel.refreshOrders()
                            }
                        ) {
                            Text("Retry")
                        }
                    }
                }

                filteredOrders.isEmpty() -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "No orders found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "There are no ${tabs[selectedTab].lowercase()} orders.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        )
                    ) {

                        items(
                            items = filteredOrders,
                            key = {
                                it.order_id
                            }
                        ) { order ->

                            OrderCard(
                                order = order,
                                onApprove = {
                                    viewModel.updateOrder(
                                        orderId = order.order_id,
                                        isApproved = 1,
                                        quantity = order.quantity,
                                        message = order.message
                                    )
                                },
                                onReject = {
                                    viewModel.updateOrder(
                                        orderId = order.order_id,
                                        isApproved = 0,
                                        quantity = order.quantity,
                                        message = order.message
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(
    order: Order,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {

    val isApproved = order.isApproved == 1

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = if (!isApproved) {
                        Icons.Default.ShoppingCart
                    } else {
                        Icons.Default.LocalShipping
                    },
                    contentDescription = null,
                    modifier = Modifier.size(42.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.size(12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = order.product_name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Order ID: ${order.order_id}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OrderStatusChip(
                    status = if (isApproved) {
                        "Approved"
                    } else {
                        "Pending"
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OrderInfoRow(
                label = "Customer",
                value = order.user_name
            )

            OrderInfoRow(
                label = "User ID",
                value = order.user_id
            )

            OrderInfoRow(
                label = "Product ID",
                value = order.product_id
            )

            OrderInfoRow(
                label = "Category",
                value = order.category
            )

            OrderInfoRow(
                label = "Quantity",
                value = order.quantity.toString()
            )

            OrderInfoRow(
                label = "Price",
                value = "₹${"%.2f".format(order.price)}"
            )

            OrderInfoRow(
                label = "Total",
                value = "₹${"%.2f".format(order.total_amount)}"
            )

            OrderInfoRow(
                label = "Date",
                value = order.date_of_order_creation
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Message",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = order.message.ifBlank {
                    "No message"
                },
                style = MaterialTheme.typography.bodyMedium
            )

            if (!isApproved) {

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Button(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f)
                    ) {

                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(
                            modifier = Modifier.size(6.dp)
                        )

                        Text("Approve")
                    }

                    TextButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f)
                    ) {

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(
                            modifier = Modifier.size(6.dp)
                        )

                        Text("Reject")
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderStatusChip(
    status: String
) {

    FilterChip(
        selected = false,
        onClick = {},
        label = {
            Text(status)
        },
        leadingIcon = {

            Icon(
                imageVector = if (status == "Pending") {
                    Icons.Default.ShoppingCart
                } else {
                    Icons.Default.CheckCircle
                },
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    )
}

@Composable
private fun OrderInfoRow(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}