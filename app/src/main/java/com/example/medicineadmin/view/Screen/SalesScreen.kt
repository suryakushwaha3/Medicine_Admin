package com.example.medicineadmin.view.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.medicineadmin.model.Sale
import com.example.medicineadmin.view.components.AdminBottomBar
import com.example.medicineadmin.view.navigation.Routes
import com.example.medicineadmin.viewModel.SalesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val BASE_URL =
    "https://suryakush.pythonanywhere.com/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(
    navController: NavController,
    viewModel: SalesViewModel = hiltViewModel()
) {

    // ============================================================
    // VIEWMODEL STATE
    // ============================================================

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    val tabs = listOf(
        "All Sales",
        "Today"
    )

    // ============================================================
    // FILTER SALES
    // ============================================================

    val filteredSales = uiState.sales.filter { sale ->

        val matchesTab = when (tabs[selectedTab]) {

            "Today" -> isToday(
                sale.date_of_sell
            )

            else -> true
        }

        val matchesSearch =
            sale.product_name.contains(
                searchQuery,
                ignoreCase = true
            ) ||
                    sale.sell_id.contains(
                        searchQuery,
                        ignoreCase = true
                    ) ||
                    sale.user_name.contains(
                        searchQuery,
                        ignoreCase = true
                    ) ||
                    sale.product_id.contains(
                        searchQuery,
                        ignoreCase = true
                    )

        matchesTab && matchesSearch
    }

    // ============================================================
    // SUMMARY
    // ============================================================

    val totalSales = filteredSales.sumOf {
        it.total_amount
    }

    val totalItems = filteredSales.sumOf {
        it.quantity
    }

    // ============================================================
    // SCAFFOLD
    // ============================================================

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Sales",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Sell history",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },

                actions = {

                    IconButton(
                        onClick = {
                            viewModel.refreshSales()
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Refresh,

                            contentDescription =
                                "Refresh"
                        )
                    }
                }
            )
        },

        bottomBar = {

            AdminBottomBar(
                navController = navController,
                currentRoute = Routes.Sales
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

            // ====================================================
            // SUMMARY CARDS
            // ====================================================

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),

                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                SalesSummaryCard(
                    title = "Total Sales",

                    value =
                        "₹${"%.2f".format(
                            totalSales
                        )}",

                    modifier =
                        Modifier.weight(1f)
                )

                SalesSummaryCard(
                    title = "Items Sold",

                    value =
                        totalItems.toString(),

                    modifier =
                        Modifier.weight(1f)
                )
            }

            // ====================================================
            // SEARCH
            // ====================================================

            OutlinedTextField(

                value = searchQuery,

                onValueChange = {
                    searchQuery = it
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),

                singleLine = true,

                placeholder = {
                    Text("Search sales...")
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Default.Search,

                        contentDescription =
                            "Search"
                    )
                },

                shape =
                    RoundedCornerShape(14.dp)
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            // ====================================================
            // TABS
            // ====================================================

            TabRow(
                selectedTabIndex = selectedTab
            ) {

                tabs.forEachIndexed { index, title ->

                    Tab(

                        selected =
                            selectedTab == index,

                        onClick = {
                            selectedTab = index
                        },

                        text = {
                            Text(title)
                        }
                    )
                }
            }

            // ====================================================
            // LOADING
            // ====================================================

            if (uiState.isLoading) {

                Column(

                    modifier =
                        Modifier.fillMaxSize(),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    CircularProgressIndicator()

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text =
                            "Loading sales...",

                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                    )
                }

            }

            // ====================================================
            // ERROR
            // ====================================================

            else if (
                uiState.error != null &&
                uiState.sales.isEmpty()
            ) {

                Column(

                    modifier =
                        Modifier.fillMaxSize(),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.PointOfSale,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(60.dp),

                        tint =
                            MaterialTheme.colorScheme.error
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(

                        text =
                            uiState.error
                                ?: "Something went wrong",

                        style =
                            MaterialTheme.typography
                                .titleMedium,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    IconButton(

                        onClick = {
                            viewModel.refreshSales()
                        }
                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Refresh,

                            contentDescription =
                                "Retry"
                        )
                    }

                    Text(

                        text =
                            "Tap refresh to retry",

                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                    )
                }

            }

            // ====================================================
            // EMPTY
            // ====================================================

            else if (filteredSales.isEmpty()) {

                Column(

                    modifier =
                        Modifier.fillMaxSize(),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.PointOfSale,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(60.dp),

                        tint =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(

                        text =
                            "No sales found",

                        style =
                            MaterialTheme.typography
                                .titleMedium,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(

                        text =
                            if (searchQuery.isBlank()) {
                                "No sales records available."
                            } else {
                                "No matching sales records."
                            },

                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                    )
                }

            }

            // ====================================================
            // SALES LIST
            // ====================================================

            else {

                LazyColumn(

                    modifier =
                        Modifier.fillMaxSize(),

                    verticalArrangement =
                        Arrangement.spacedBy(10.dp),

                    contentPadding =
                        PaddingValues(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        )
                ) {

                    items(

                        items = filteredSales,

                        key = {
                            it.sell_id
                        }

                    ) { sale ->

                        SaleCard(
                            sale = sale
                        )
                    }
                }
            }
        }
    }
}


// ================================================================
// CHECK TODAY
// ================================================================

private fun isToday(
    date: String
): Boolean {

    if (date.isBlank()) {
        return false
    }

    val todayFormats = listOf(
        "dd MMM yyyy",
        "dd-MM-yyyy",
        "dd/MM/yyyy",
        "yyyy-MM-dd",
        "yyyy/MM/dd"
    )

    val today = Date()

    for (format in todayFormats) {

        try {

            val formatter =
                SimpleDateFormat(
                    format,
                    Locale.ENGLISH
                )

            formatter.isLenient = false

            val todayString =
                formatter.format(today)

            if (
                date.trim().equals(
                    todayString,
                    ignoreCase = true
                )
            ) {
                return true
            }

        } catch (_: Exception) {
            // Try next format
        }
    }

    return false
}


// ================================================================
// SUMMARY CARD
// ================================================================

@Composable
private fun SalesSummaryCard(

    title: String,

    value: String,

    modifier: Modifier = Modifier

) {

    Card(

        modifier = modifier,

        shape =
            RoundedCornerShape(18.dp),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(

                text = title,

                style =
                    MaterialTheme.typography.bodyMedium,

                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(

                text = value,

                style =
                    MaterialTheme.typography.titleLarge,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


// ================================================================
// SALE CARD
// ================================================================

@Composable
private fun SaleCard(
    sale: Sale
) {

    /*
     * Backend image:
     *
     * uploads/products/xxxx.jpg
     *
     * Final URL:
     *
     * https://suryakush.pythonanywhere.com/uploads/products/xxxx.jpg
     */

    val imageUrl = sale.product_image
        ?.trim()
        ?.takeIf {
            it.isNotEmpty()
        }
        ?.let {

            if (
                it.startsWith(
                    "http://",
                    ignoreCase = true
                ) ||
                it.startsWith(
                    "https://",
                    ignoreCase = true
                )
            ) {

                it

            } else {

                BASE_URL +
                        it.removePrefix("/")
            }
        }

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Column(

            modifier =
                Modifier.padding(16.dp)
        ) {

            // ====================================================
            // PRODUCT HEADER
            // ====================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                // =================================================
                // PRODUCT IMAGE
                // =================================================

                Box(

                    modifier = Modifier
                        .size(64.dp)
                        .clip(
                            RoundedCornerShape(14.dp)
                        )
                        .background(
                            MaterialTheme.colorScheme
                                .primaryContainer
                        ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    if (imageUrl != null) {

                        AsyncImage(

                            model =
                                imageUrl,

                            contentDescription =
                                sale.product_name,

                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .clip(
                                        RoundedCornerShape(14.dp)
                                    ),

                            contentScale =
                                ContentScale.Crop
                        )

                    } else {

                        Icon(

                            imageVector =
                                Icons.Default.Image,

                            contentDescription =
                                "No product image",

                            modifier =
                                Modifier.size(30.dp),

                            tint =
                                MaterialTheme.colorScheme
                                    .onPrimaryContainer
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.size(12.dp)
                )

                // =================================================
                // PRODUCT DETAILS
                // =================================================

                Column(

                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(

                        text =
                            sale.product_name,

                        style =
                            MaterialTheme.typography
                                .titleMedium,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(2.dp)
                    )

                    Text(

                        text =
                            sale.product_id,

                        style =
                            MaterialTheme.typography
                                .bodySmall,

                        color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                    )
                }

                Spacer(
                    modifier =
                        Modifier.size(8.dp)
                )

                // =================================================
                // TOTAL AMOUNT
                // =================================================

                Text(

                    text =
                        "₹${"%.2f".format(
                            sale.total_amount
                        )}",

                    style =
                        MaterialTheme.typography
                            .titleMedium,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            // ====================================================
            // SALE INFORMATION
            // ====================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                SaleInfo(

                    title =
                        "Sale ID",

                    value =
                        sale.sell_id
                )

                SaleInfo(

                    title =
                        "Quantity",

                    value =
                        sale.quantity.toString()
                )

                SaleInfo(

                    title =
                        "Price",

                    value =
                        "₹${"%.2f".format(
                            sale.price
                        )}"
                )
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            // ====================================================
            // DATE + STOCK
            // ====================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(

                    imageVector =
                        Icons.Default.CalendarToday,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(16.dp),

                    tint =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )

                Spacer(
                    modifier =
                        Modifier.size(6.dp)
                )

                Text(

                    text =
                        sale.date_of_sell,

                    style =
                        MaterialTheme.typography
                            .bodySmall,

                    color =
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
                )

                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )

                Text(

                    text =
                        "Stock: ${sale.reamining_stock}",

                    style =
                        MaterialTheme.typography
                            .bodySmall,

                    fontWeight =
                        FontWeight.SemiBold
                )
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            // ====================================================
            // CUSTOMER
            // ====================================================

            Text(

                text =
                    "Customer: ${sale.user_name}",

                style =
                    MaterialTheme.typography.bodyMedium
            )

            Text(

                text =
                    "User ID: ${sale.user_id}",

                style =
                    MaterialTheme.typography.bodySmall,

                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
            )
        }
    }
}


// ================================================================
// SALE INFO
// ================================================================

@Composable
private fun SaleInfo(

    title: String,

    value: String

) {

    Column {

        Text(

            text = title,

            style =
                MaterialTheme.typography.labelSmall,

            color =
                MaterialTheme.colorScheme
                    .onSurfaceVariant
        )

        Text(

            text = value,

            style =
                MaterialTheme.typography.bodyMedium,

            fontWeight =
                FontWeight.SemiBold
        )
    }
}