//package com.example.medicineadmin.view.Screen
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AddBox
//import androidx.compose.material.icons.filled.Assignment
//import androidx.compose.material.icons.filled.Block
//import androidx.compose.material.icons.filled.CheckCircle
//import androidx.compose.material.icons.filled.ErrorOutline
//import androidx.compose.material.icons.filled.Group
//import androidx.compose.material.icons.filled.Inventory2
//import androidx.compose.material.icons.filled.LocalShipping
//import androidx.compose.material.icons.filled.PointOfSale
//import androidx.compose.material.icons.filled.Refresh
//import androidx.compose.material.icons.filled.ShoppingBag
//import androidx.compose.material.icons.filled.Warning
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.navigation.NavController
//import com.example.medicineadmin.model.Sale
//import com.example.medicineadmin.view.components.AdminBottomBar
//import com.example.medicineadmin.view.components.AdminBottomScreen
//import com.example.medicineadmin.view.components.AdminTopBar
//import com.example.medicineadmin.view.navigation.Routes
//import com.example.medicineadmin.viewModel.DashboardUiState
//import com.example.medicineadmin.viewModel.DashboardViewModel
//import com.example.medicineadmin.viewModel.NotificationViewModel
//import java.text.SimpleDateFormat
//import java.util.Locale
//
//
//@Composable
//fun DashboardScreen(
//    navController: NavController,
//    viewModel: DashboardViewModel = hiltViewModel(),
//    notificationViewModel: NotificationViewModel = hiltViewModel()
//) {
//
//    // ============================================================
//    // DASHBOARD STATE
//    // ============================================================
//
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//
//
//    // ============================================================
//    // NOTIFICATION STATE
//    // ============================================================
//
//    val notificationUiState by notificationViewModel.uiState
//        .collectAsStateWithLifecycle()
//
//
//    // ============================================================
//    // LOAD UNREAD NOTIFICATION COUNT
//    // ============================================================
//
//    LaunchedEffect(Unit) {
//        notificationViewModel.getUnreadNotificationCount()
//    }
//
//
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//
//        topBar = {
//
//            AdminTopBar(
//
//                onMenuClick = {
//                    // Menu action
//                },
//
//                onNotificationClick = {
//
//                    navController.navigate(
//                        Routes.Notifications
//                    )
//                },
//
//                onProfileClick = {
//
//                    navController.navigate(
//                        Routes.Settings
//                    )
//                },
//
//                notificationCount = notificationUiState.unreadCount
//            )
//        },
//
//        bottomBar = {
//
//            AdminBottomBar(
//                navController = navController,
//                currentRoute = Routes.Dashboard
//            )
//        },
//
//        containerColor = Color(0xFFF6F8FC)
//
//    ) { innerPadding ->
//
//        when {
//
//            // ====================================================
//            // LOADING
//            // ====================================================
//
//            uiState.isLoading &&
//                    uiState.users.isEmpty() -> {
//
//                DashboardLoading(
//                    paddingValues = innerPadding
//                )
//            }
//
//
//            // ====================================================
//            // ERROR
//            // ====================================================
//
//            uiState.error != null &&
//                    uiState.users.isEmpty() -> {
//
//                DashboardError(
//                    paddingValues = innerPadding,
//                    message = uiState.error
//                        ?: "Something went wrong.",
//                    onRetry = {
//                        viewModel.refreshDashboard()
//                    }
//                )
//            }
//
//
//            // ====================================================
//            // CONTENT
//            // ====================================================
//
//            else -> {
//
//                DashboardContent(
//                    paddingValues = innerPadding,
//                    uiState = uiState,
//                    navController = navController
//                )
//            }
//        }
//    }
//}
//
//
//// =================================================================
//// DASHBOARD CONTENT
//// =================================================================
//
//@Composable
//private fun DashboardContent(
//    paddingValues: PaddingValues,
//    uiState: DashboardUiState,
//    navController: NavController
//) {
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(paddingValues)
//            .verticalScroll(rememberScrollState())
//            .padding(
//                horizontal = 16.dp,
//                vertical = 14.dp
//            )
//    ) {
//
//        WelcomeBanner(
//            totalUsers = uiState.totalUsers,
//            totalProducts = uiState.totalProducts,
//            totalOrders = uiState.orders.size
//        )
//
//        Spacer(
//            modifier = Modifier.height(18.dp)
//        )
//
//
//        SectionTitle(
//            title = "Overview",
//            subtitle = "Your shop at a glance"
//        )
//
//        Spacer(
//            modifier = Modifier.height(10.dp)
//        )
//
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//
//            DashboardStatCard(
//                title = "Total Users",
//                value = uiState.totalUsers.toString(),
//                subtitle = "${uiState.approvedUsers} approved",
//                icon = Icons.Default.Group,
//                iconBackground = Color(0xFFE7F0FF),
//                iconColor = Color(0xFF1479F2),
//                modifier = Modifier.weight(1f)
//            )
//
//
//            DashboardStatCard(
//                title = "Products",
//                value = uiState.totalProducts.toString(),
//                subtitle = "${uiState.lowStock} low stock",
//                icon = Icons.Default.Inventory2,
//                iconBackground = Color(0xFFE9FAF0),
//                iconColor = Color(0xFF159447),
//                modifier = Modifier.weight(1f)
//            )
//        }
//
//
//        Spacer(
//            modifier = Modifier.height(12.dp)
//        )
//
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//
//            DashboardStatCard(
//                title = "Orders",
//                value = uiState.orders.size.toString(),
//                subtitle = "${uiState.pendingOrders} pending",
//                icon = Icons.Default.ShoppingBag,
//                iconBackground = Color(0xFFFFF3DF),
//                iconColor = Color(0xFFE98A00),
//                modifier = Modifier.weight(1f)
//            )
//
//
//            DashboardStatCard(
//                title = "Total Sales",
//                value = "₹${formatAmount(uiState.totalSales)}",
//                subtitle = "${uiState.sales.sumOf { it.quantity }} items sold",
//                icon = Icons.Default.PointOfSale,
//                iconBackground = Color(0xFFF0EAFE),
//                iconColor = Color(0xFF7B4DD8),
//                modifier = Modifier.weight(1f)
//            )
//        }
//
//
//        Spacer(
//            modifier = Modifier.height(20.dp)
//        )
//
//
//        SectionTitle(
//            title = "Attention Required",
//            subtitle = "Items that may need your action"
//        )
//
//        Spacer(
//            modifier = Modifier.height(10.dp)
//        )
//
//
//        AttentionCard(
//            title = "Pending User Approvals",
//            value = uiState.pendingUsers.toString(),
//            description = if (uiState.pendingUsers == 0) {
//                "All users are approved"
//            } else {
//                "Users waiting for approval"
//            },
//            icon = Icons.Default.Group,
//            backgroundColor = Color(0xFFFFF5E8),
//            iconColor = Color(0xFFE58A00),
//            onClick = {
//
//                navController.navigate(
//                    AdminBottomScreen.Users.route
//                )
//            }
//        )
//
//
//        Spacer(
//            modifier = Modifier.height(10.dp)
//        )
//
//
//        AttentionCard(
//            title = "Pending Orders",
//            value = uiState.pendingOrders.toString(),
//            description = if (uiState.pendingOrders == 0) {
//                "No orders waiting"
//            } else {
//                "Orders waiting for approval"
//            },
//            icon = Icons.Default.Assignment,
//            backgroundColor = Color(0xFFFFEEEE),
//            iconColor = Color(0xFFD93A3A),
//            onClick = {
//
//                navController.navigate(
//                    AdminBottomScreen.Orders.route
//                )
//            }
//        )
//
//
//        Spacer(
//            modifier = Modifier.height(10.dp)
//        )
//
//
//        AttentionCard(
//            title = "Low Stock Products",
//            value = uiState.lowStock.toString(),
//            description = if (uiState.lowStock == 0) {
//                "Stock levels look good"
//            } else {
//                "Products need stock attention"
//            },
//            icon = Icons.Default.Warning,
//            backgroundColor = Color(0xFFFFF8E5),
//            iconColor = Color(0xFFD99800),
//            onClick = {
//
//                navController.navigate(
//                    AdminBottomScreen.Products.route
//                )
//            }
//        )
//
//
//        Spacer(
//            modifier = Modifier.height(20.dp)
//        )
//
//
//        SectionTitle(
//            title = "User Status",
//            subtitle = "Current customer account status"
//        )
//
//        Spacer(
//            modifier = Modifier.height(10.dp)
//        )
//
//
//        UserStatusCard(
//            approved = uiState.approvedUsers,
//            pending = uiState.pendingUsers,
//            blocked = uiState.blockedUsers,
//            total = uiState.totalUsers
//        )
//
//
//        Spacer(
//            modifier = Modifier.height(20.dp)
//        )
//
//
//        SectionTitle(
//            title = "Order Status",
//            subtitle = "Current order overview"
//        )
//
//        Spacer(
//            modifier = Modifier.height(10.dp)
//        )
//
//
//        OrderStatusCard(
//            pending = uiState.pendingOrders,
//            approved = uiState.approvedOrders,
//            completed = uiState.completedOrders,
//            total = uiState.orders.size
//        )
//
//
//        Spacer(
//            modifier = Modifier.height(20.dp)
//        )
//
//
//        SectionTitle(
//            title = "Quick Actions",
//            subtitle = "Manage your store quickly"
//        )
//
//        Spacer(
//            modifier = Modifier.height(10.dp)
//        )
//
//
//        LazyRow(
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            contentPadding = PaddingValues(
//                horizontal = 2.dp
//            )
//        ) {
//
//            item {
//
//                QuickActionItem(
//                    title = "Users",
//                    icon = Icons.Default.Group,
//                    background = Color(0xFFF0EAFE),
//                    iconColor = Color(0xFF7B4DD8),
//                    onClick = {
//
//                        navController.navigate(
//                            AdminBottomScreen.Users.route
//                        )
//                    }
//                )
//            }
//
//
//            item {
//
//                QuickActionItem(
//                    title = "Products",
//                    icon = Icons.Default.AddBox,
//                    background = Color(0xFFE9FAF0),
//                    iconColor = Color(0xFF159447),
//                    onClick = {
//
//                        navController.navigate(
//                            AdminBottomScreen.Products.route
//                        )
//                    }
//                )
//            }
//
//
//            item {
//
//                QuickActionItem(
//                    title = "Orders",
//                    icon = Icons.Default.LocalShipping,
//                    background = Color(0xFFFFF3DF),
//                    iconColor = Color(0xFFE98A00),
//                    onClick = {
//
//                        navController.navigate(
//                            AdminBottomScreen.Orders.route
//                        )
//                    }
//                )
//            }
//
//
//            item {
//
//                QuickActionItem(
//                    title = "Sales",
//                    icon = Icons.Default.PointOfSale,
//                    background = Color(0xFFE7F0FF),
//                    iconColor = Color(0xFF1479F2),
//                    onClick = {
//
//                        navController.navigate(
//                            AdminBottomScreen.Sales.route
//                        )
//                    }
//                )
//            }
//        }
//
//
//        Spacer(
//            modifier = Modifier.height(20.dp)
//        )
//
//
//        SectionTitle(
//            title = "Recent Sales",
//            subtitle = "Latest transactions"
//        )
//
//        Spacer(
//            modifier = Modifier.height(10.dp)
//        )
//
//
//        if (uiState.sales.isEmpty()) {
//
//            EmptySalesCard()
//
//        } else {
//
//            uiState.sales
//                .takeLast(5)
//                .reversed()
//                .forEach { sale ->
//
//                    RecentSaleCard(
//                        sale = sale
//                    )
//
//                    Spacer(
//                        modifier = Modifier.height(10.dp)
//                    )
//                }
//        }
//
//
//        Spacer(
//            modifier = Modifier.height(20.dp)
//        )
//    }
//}
//
//
//// =================================================================
//// WELCOME BANNER
//// =================================================================
//
//@Composable
//private fun WelcomeBanner(
//    totalUsers: Int,
//    totalProducts: Int,
//    totalOrders: Int
//) {
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(
//                RoundedCornerShape(22.dp)
//            )
//            .background(
//                Color(0xFF1479F2)
//            )
//            .padding(20.dp)
//    ) {
//
//        Column {
//
//            Text(
//                text = "Hello, Admin 👋",
//                color = Color.White,
//                fontSize = 22.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//
//            Spacer(
//                modifier = Modifier.height(5.dp)
//            )
//
//
//            Text(
//                text = "Here's what's happening in your medical shop today.",
//                color = Color.White.copy(alpha = 0.88f),
//                fontSize = 13.sp
//            )
//
//
//            Spacer(
//                modifier = Modifier.height(18.dp)
//            )
//
//
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(24.dp)
//            ) {
//
//                MiniBannerStat(
//                    value = totalUsers.toString(),
//                    label = "Users"
//                )
//
//                MiniBannerStat(
//                    value = totalProducts.toString(),
//                    label = "Products"
//                )
//
//                MiniBannerStat(
//                    value = totalOrders.toString(),
//                    label = "Orders"
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//private fun MiniBannerStat(
//    value: String,
//    label: String
//) {
//
//    Column {
//
//        Text(
//            text = value,
//            color = Color.White,
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold
//        )
//
//
//        Text(
//            text = label,
//            color = Color.White.copy(alpha = 0.75f),
//            fontSize = 11.sp
//        )
//    }
//}
//
//
//// =================================================================
//// SECTION TITLE
//// =================================================================
//
//@Composable
//private fun SectionTitle(
//    title: String,
//    subtitle: String
//) {
//
//    Column {
//
//        Text(
//            text = title,
//            fontSize = 17.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF123B78)
//        )
//
//
//        Text(
//            text = subtitle,
//            fontSize = 11.sp,
//            color = Color(0xFF71839C)
//        )
//    }
//}
//
//
//// =================================================================
//// DASHBOARD STAT CARD
//// =================================================================
//
//@Composable
//private fun DashboardStatCard(
//    title: String,
//    value: String,
//    subtitle: String,
//    icon: ImageVector,
//    iconBackground: Color,
//    iconColor: Color,
//    modifier: Modifier = Modifier
//) {
//
//    Card(
//        modifier = modifier,
//        shape = RoundedCornerShape(18.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        ),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 2.dp
//        )
//    ) {
//
//        Column(
//            modifier = Modifier.padding(15.dp)
//        ) {
//
//            Box(
//                modifier = Modifier
//                    .size(42.dp)
//                    .clip(
//                        RoundedCornerShape(13.dp)
//                    )
//                    .background(
//                        iconBackground
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//
//                Icon(
//                    imageVector = icon,
//                    contentDescription = null,
//                    tint = iconColor,
//                    modifier = Modifier.size(22.dp)
//                )
//            }
//
//
//            Spacer(
//                modifier = Modifier.height(12.dp)
//            )
//
//
//            Text(
//                text = title,
//                fontSize = 12.sp,
//                color = Color(0xFF71839C),
//                fontWeight = FontWeight.Medium
//            )
//
//
//            Spacer(
//                modifier = Modifier.height(4.dp)
//            )
//
//
//            Text(
//                text = value,
//                fontSize = 20.sp,
//                color = Color(0xFF123B78),
//                fontWeight = FontWeight.Bold,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//
//
//            Spacer(
//                modifier = Modifier.height(3.dp)
//            )
//
//
//            Text(
//                text = subtitle,
//                fontSize = 10.sp,
//                color = iconColor,
//                fontWeight = FontWeight.SemiBold
//            )
//        }
//    }
//}
//
//
//// =================================================================
//// ATTENTION CARD
//// =================================================================
//
//@Composable
//private fun AttentionCard(
//    title: String,
//    value: String,
//    description: String,
//    icon: ImageVector,
//    backgroundColor: Color,
//    iconColor: Color,
//    onClick: () -> Unit
//) {
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable {
//                onClick()
//            },
//        shape = RoundedCornerShape(18.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        ),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 1.dp
//        )
//    ) {
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(14.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            Box(
//                modifier = Modifier
//                    .size(44.dp)
//                    .clip(
//                        RoundedCornerShape(13.dp)
//                    )
//                    .background(
//                        backgroundColor
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//
//                Icon(
//                    imageVector = icon,
//                    contentDescription = null,
//                    tint = iconColor,
//                    modifier = Modifier.size(22.dp)
//                )
//            }
//
//
//            Spacer(
//                modifier = Modifier.width(12.dp)
//            )
//
//
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//
//                Text(
//                    text = title,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFF123B78)
//                )
//
//
//                Text(
//                    text = description,
//                    fontSize = 11.sp,
//                    color = Color(0xFF71839C)
//                )
//            }
//
//
//            Text(
//                text = value,
//                fontSize = 21.sp,
//                fontWeight = FontWeight.Bold,
//                color = iconColor
//            )
//        }
//    }
//}
//
//
//// =================================================================
//// USER STATUS CARD
//// =================================================================
//
//@Composable
//private fun UserStatusCard(
//    approved: Int,
//    pending: Int,
//    blocked: Int,
//    total: Int
//) {
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(18.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        ),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 2.dp
//        )
//    ) {
//
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//
//            StatusRow(
//                title = "Approved Users",
//                value = approved,
//                total = total,
//                color = Color(0xFF159447),
//                icon = Icons.Default.CheckCircle
//            )
//
//
//            Spacer(
//                modifier = Modifier.height(14.dp)
//            )
//
//
//            StatusRow(
//                title = "Pending Users",
//                value = pending,
//                total = total,
//                color = Color(0xFFE98A00),
//                icon = Icons.Default.Assignment
//            )
//
//
//            Spacer(
//                modifier = Modifier.height(14.dp)
//            )
//
//
//            StatusRow(
//                title = "Blocked Users",
//                value = blocked,
//                total = total,
//                color = Color(0xFFD93A3A),
//                icon = Icons.Default.Block
//            )
//        }
//    }
//}
//
//
//// =================================================================
//// ORDER STATUS CARD
//// =================================================================
//
//@Composable
//private fun OrderStatusCard(
//    pending: Int,
//    approved: Int,
//    completed: Int,
//    total: Int
//) {
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(18.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        ),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 2.dp
//        )
//    ) {
//
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//
//            StatusRow(
//                title = "Pending Orders",
//                value = pending,
//                total = total,
//                color = Color(0xFFE98A00),
//                icon = Icons.Default.Assignment
//            )
//
//
//            Spacer(
//                modifier = Modifier.height(14.dp)
//            )
//
//
//            StatusRow(
//                title = "Approved Orders",
//                value = approved,
//                total = total,
//                color = Color(0xFF1479F2),
//                icon = Icons.Default.CheckCircle
//            )
//
//
//            Spacer(
//                modifier = Modifier.height(14.dp)
//            )
//
//
//            StatusRow(
//                title = "Completed Orders",
//                value = completed,
//                total = total,
//                color = Color(0xFF159447),
//                icon = Icons.Default.LocalShipping
//            )
//        }
//    }
//}
//
//
//// =================================================================
//// STATUS ROW
//// =================================================================
//
//@Composable
//private fun StatusRow(
//    title: String,
//    value: Int,
//    total: Int,
//    color: Color,
//    icon: ImageVector
//) {
//
//    val progress = remember(value, total) {
//
//        if (total > 0) {
//
//            (
//                    value.toFloat() /
//                            total.toFloat()
//                    )
//                .coerceIn(0f, 1f)
//
//        } else {
//            0f
//        }
//    }
//
//
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        Box(
//            modifier = Modifier
//                .size(36.dp)
//                .clip(CircleShape)
//                .background(
//                    color.copy(alpha = 0.12f)
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//
//            Icon(
//                imageVector = icon,
//                contentDescription = null,
//                tint = color,
//                modifier = Modifier.size(19.dp)
//            )
//        }
//
//
//        Spacer(
//            modifier = Modifier.width(10.dp)
//        )
//
//
//        Column(
//            modifier = Modifier.weight(1f)
//        ) {
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//
//                Text(
//                    text = title,
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.SemiBold,
//                    color = Color(0xFF45617F)
//                )
//
//
//                Text(
//                    text = value.toString(),
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = color
//                )
//            }
//
//
//            Spacer(
//                modifier = Modifier.height(6.dp)
//            )
//
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(6.dp)
//                    .clip(
//                        RoundedCornerShape(10.dp)
//                    )
//                    .background(
//                        Color(0xFFEAF0F6)
//                    )
//            ) {
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth(progress)
//                        .height(6.dp)
//                        .clip(
//                            RoundedCornerShape(10.dp)
//                        )
//                        .background(color)
//                )
//            }
//        }
//    }
//}
//
//
//// =================================================================
//// QUICK ACTION
//// =================================================================
//
//@Composable
//private fun QuickActionItem(
//    title: String,
//    icon: ImageVector,
//    background: Color,
//    iconColor: Color,
//    onClick: () -> Unit
//) {
//
//    Card(
//        modifier = Modifier
//            .width(88.dp)
//            .clickable {
//                onClick()
//            },
//        shape = RoundedCornerShape(18.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        ),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 2.dp
//        )
//    ) {
//
//        Column(
//            modifier = Modifier.padding(
//                vertical = 14.dp,
//                horizontal = 8.dp
//            ),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Box(
//                modifier = Modifier
//                    .size(46.dp)
//                    .clip(
//                        RoundedCornerShape(14.dp)
//                    )
//                    .background(background),
//                contentAlignment = Alignment.Center
//            ) {
//
//                Icon(
//                    imageVector = icon,
//                    contentDescription = title,
//                    tint = iconColor,
//                    modifier = Modifier.size(22.dp)
//                )
//            }
//
//
//            Spacer(
//                modifier = Modifier.height(8.dp)
//            )
//
//
//            Text(
//                text = title,
//                fontSize = 11.sp,
//                fontWeight = FontWeight.SemiBold,
//                color = Color(0xFF123B78),
//                maxLines = 1
//            )
//        }
//    }
//}
//
//
//// =================================================================
//// RECENT SALE
//// =================================================================
//
//@Composable
//private fun RecentSaleCard(
//    sale: Sale
//) {
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(18.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        ),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 1.dp
//        )
//    ) {
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(14.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            Box(
//                modifier = Modifier
//                    .size(44.dp)
//                    .clip(
//                        RoundedCornerShape(13.dp)
//                    )
//                    .background(
//                        Color(0xFFE7F0FF)
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//
//                Icon(
//                    imageVector = Icons.Default.PointOfSale,
//                    contentDescription = null,
//                    tint = Color(0xFF1479F2),
//                    modifier = Modifier.size(21.dp)
//                )
//            }
//
//
//            Spacer(
//                modifier = Modifier.width(12.dp)
//            )
//
//
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//
//                Text(
//                    text = sale.product_name,
//                    fontSize = 13.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFF123B78),
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//
//                Spacer(
//                    modifier = Modifier.height(2.dp)
//                )
//
//
//                Text(
//                    text = "${sale.user_name} • Qty ${sale.quantity}",
//                    fontSize = 10.sp,
//                    color = Color(0xFF71839C),
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//
//                Text(
//                    text = formatSaleDate(
//                        sale.date_of_sell
//                    ),
//                    fontSize = 10.sp,
//                    color = Color(0xFF9AA9BB)
//                )
//            }
//
//
//            Text(
//                text = "₹${formatAmount(sale.total_amount)}",
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFF159447)
//            )
//        }
//    }
//}
//
//
//// =================================================================
//// EMPTY SALES
//// =================================================================
//
//@Composable
//private fun EmptySalesCard() {
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(18.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        )
//    ) {
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Icon(
//                imageVector = Icons.Default.PointOfSale,
//                contentDescription = null,
//                modifier = Modifier.size(40.dp),
//                tint = Color(0xFF9AA9BB)
//            )
//
//
//            Spacer(
//                modifier = Modifier.height(8.dp)
//            )
//
//
//            Text(
//                text = "No sales yet",
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFF45617F)
//            )
//
//
//            Text(
//                text = "Recent transactions will appear here.",
//                fontSize = 11.sp,
//                color = Color(0xFF9AA9BB)
//            )
//        }
//    }
//}
//
//
//// =================================================================
//// LOADING
//// =================================================================
//
//@Composable
//private fun DashboardLoading(
//    paddingValues: PaddingValues
//) {
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(paddingValues),
//        contentAlignment = Alignment.Center
//    ) {
//
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            CircularProgressIndicator(
//                color = Color(0xFF1479F2)
//            )
//
//
//            Spacer(
//                modifier = Modifier.height(12.dp)
//            )
//
//
//            Text(
//                text = "Loading dashboard...",
//                color = Color(0xFF71839C),
//                fontSize = 13.sp
//            )
//        }
//    }
//}
//
//
//// =================================================================
//// ERROR
//// =================================================================
//
//@Composable
//private fun DashboardError(
//    paddingValues: PaddingValues,
//    message: String,
//    onRetry: () -> Unit
//) {
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(paddingValues)
//            .padding(24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//
//        Icon(
//            imageVector = Icons.Default.ErrorOutline,
//            contentDescription = null,
//            modifier = Modifier.size(60.dp),
//            tint = Color(0xFFD93A3A)
//        )
//
//
//        Spacer(
//            modifier = Modifier.height(10.dp)
//        )
//
//
//        Text(
//            text = "Unable to load dashboard",
//            fontSize = 17.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF123B78)
//        )
//
//
//        Spacer(
//            modifier = Modifier.height(5.dp)
//        )
//
//
//        Text(
//            text = message,
//            fontSize = 12.sp,
//            color = Color(0xFF71839C)
//        )
//
//
//        Spacer(
//            modifier = Modifier.height(14.dp)
//        )
//
//
//        IconButton(
//            onClick = onRetry
//        ) {
//
//            Icon(
//                imageVector = Icons.Default.Refresh,
//                contentDescription = "Retry",
//                tint = Color(0xFF1479F2)
//            )
//        }
//
//
//        Text(
//            text = "Tap refresh to try again",
//            fontSize = 11.sp,
//            color = Color(0xFF71839C)
//        )
//    }
//}
//
//
//// =================================================================
//// FORMAT AMOUNT
//// =================================================================
//
//private fun formatAmount(
//    amount: Double
//): String {
//
//    return String.format(
//        Locale.getDefault(),
//        "%.2f",
//        amount
//    )
//}
//
//
//// =================================================================
//// FORMAT SALE DATE
//// =================================================================
//
//private fun formatSaleDate(
//    date: String
//): String {
//
//    if (date.isBlank()) {
//        return "Date unavailable"
//    }
//
//
//    val formats = listOf(
//        "yyyy-MM-dd",
//        "yyyy/MM/dd",
//        "dd-MM-yyyy",
//        "dd/MM/yyyy",
//        "dd MMM yyyy",
//        "dd MMMM yyyy"
//    )
//
//
//    val outputFormat = SimpleDateFormat(
//        "dd MMM yyyy",
//        Locale.ENGLISH
//    )
//
//
//    for (format in formats) {
//
//        try {
//
//            val inputFormat = SimpleDateFormat(
//                format,
//                Locale.ENGLISH
//            )
//
//            inputFormat.isLenient = false
//
//            val parsed = inputFormat.parse(
//                date.trim()
//            )
//
//            if (parsed != null) {
//                return outputFormat.format(parsed)
//            }
//
//        } catch (_: Exception) {
//            // Try next format
//        }
//    }
//
//
//    return date
//}


package com.example.medicineadmin.view.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.medicineadmin.model.Sale
import com.example.medicineadmin.view.components.AdminBottomBar
import com.example.medicineadmin.view.components.AdminBottomScreen
import com.example.medicineadmin.view.components.AdminTopBar
import com.example.medicineadmin.view.navigation.Routes
import com.example.medicineadmin.viewModel.DashboardUiState
import com.example.medicineadmin.viewModel.DashboardViewModel
import com.example.medicineadmin.viewModel.NotificationViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val notificationUiState by notificationViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        notificationViewModel.getUnreadNotificationCount()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AdminTopBar(
                onMenuClick = {},
                onNotificationClick = {
                    navController.navigate(Routes.Notifications)
                },
                onProfileClick = {
                    navController.navigate(Routes.Settings)
                },
                notificationCount = notificationUiState.unreadCount
            )
        },
        bottomBar = {
            AdminBottomBar(
                navController = navController,
                currentRoute = Routes.Dashboard
            )
        },
        containerColor = Color(0xFFF6F8FC)
    ) { innerPadding ->
        when {
            uiState.isLoading && uiState.users.isEmpty() -> {
                DashboardLoading(innerPadding)
            }

            uiState.error != null && uiState.users.isEmpty() -> {
                DashboardError(
                    paddingValues = innerPadding,
                    message = uiState.error ?: "Something went wrong.",
                    onRetry = {
                        viewModel.refreshDashboard()
                    }
                )
            }

            else -> {
                DashboardContent(
                    paddingValues = innerPadding,
                    uiState = uiState,
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun DashboardContent(
    paddingValues: PaddingValues,
    uiState: DashboardUiState,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        WelcomeBanner(
            totalUsers = uiState.totalUsers,
            totalProducts = uiState.totalProducts,
            totalOrders = uiState.orders.size
        )

        Spacer(modifier = Modifier.height(18.dp))

        SectionTitle(
            title = "Overview",
            subtitle = "Your shop at a glance"
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardStatCard(
                title = "Total Users",
                value = uiState.totalUsers.toString(),
                subtitle = "${uiState.approvedUsers} approved",
                icon = Icons.Default.Group,
                iconBackground = Color(0xFFE7F0FF),
                iconColor = Color(0xFF1479F2),
                modifier = Modifier.weight(1f)
            )

            DashboardStatCard(
                title = "Products",
                value = uiState.totalProducts.toString(),
                subtitle = "${uiState.lowStock} low stock",
                icon = Icons.Default.Inventory2,
                iconBackground = Color(0xFFE9FAF0),
                iconColor = Color(0xFF159447),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardStatCard(
                title = "Orders",
                value = uiState.orders.size.toString(),
                subtitle = "${uiState.pendingOrders} pending",
                icon = Icons.Default.ShoppingBag,
                iconBackground = Color(0xFFFFF3DF),
                iconColor = Color(0xFFE98A00),
                modifier = Modifier.weight(1f)
            )

            DashboardStatCard(
                title = "Total Sales",
                value = "₹${formatAmount(uiState.totalSales)}",
                subtitle = "${uiState.sales.sumOf { it.quantity }} items sold",
                icon = Icons.Default.PointOfSale,
                iconBackground = Color(0xFFF0EAFE),
                iconColor = Color(0xFF7B4DD8),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        SectionTitle(
            title = "Attention Required",
            subtitle = "Items that may need your action"
        )

        Spacer(modifier = Modifier.height(10.dp))

        AttentionCard(
            title = "Pending User Approvals",
            value = uiState.pendingUsers.toString(),
            description = if (uiState.pendingUsers == 0) {
                "All users are approved"
            } else {
                "Users waiting for approval"
            },
            icon = Icons.Default.Group,
            backgroundColor = Color(0xFFFFF5E8),
            iconColor = Color(0xFFE58A00),
            onClick = {
                navController.navigate(AdminBottomScreen.Users.route)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        AttentionCard(
            title = "Pending Orders",
            value = uiState.pendingOrders.toString(),
            description = if (uiState.pendingOrders == 0) {
                "No orders waiting"
            } else {
                "Orders waiting for approval"
            },
            icon = Icons.Default.Assignment,
            backgroundColor = Color(0xFFFFEEEE),
            iconColor = Color(0xFFD93A3A),
            onClick = {
                navController.navigate(AdminBottomScreen.Orders.route)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        AttentionCard(
            title = "Low Stock Products",
            value = uiState.lowStock.toString(),
            description = if (uiState.lowStock == 0) {
                "Stock levels look good"
            } else {
                "Products need stock attention"
            },
            icon = Icons.Default.Warning,
            backgroundColor = Color(0xFFFFF8E5),
            iconColor = Color(0xFFD99800),
            onClick = {
                navController.navigate(AdminBottomScreen.Products.route)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        SectionTitle(
            title = "User Status",
            subtitle = "Current customer account status"
        )

        Spacer(modifier = Modifier.height(10.dp))

        UserStatusCard(
            approved = uiState.approvedUsers,
            pending = uiState.pendingUsers,
            blocked = uiState.blockedUsers,
            total = uiState.totalUsers
        )

        Spacer(modifier = Modifier.height(20.dp))

        SectionTitle(
            title = "Order Status",
            subtitle = "Current order overview"
        )

        Spacer(modifier = Modifier.height(10.dp))

        OrderStatusCard(
            pending = uiState.pendingOrders,
            approved = uiState.approvedOrders,
            completed = uiState.completedOrders,
            total = uiState.orders.size
        )

        Spacer(modifier = Modifier.height(20.dp))

        SectionTitle(
            title = "Quick Actions",
            subtitle = "Manage your store quickly"
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            item {
                QuickActionItem(
                    title = "Users",
                    icon = Icons.Default.Group,
                    background = Color(0xFFF0EAFE),
                    iconColor = Color(0xFF7B4DD8),
                    onClick = {
                        navController.navigate(AdminBottomScreen.Users.route)
                    }
                )
            }

            item {
                QuickActionItem(
                    title = "Products",
                    icon = Icons.Default.AddBox,
                    background = Color(0xFFE9FAF0),
                    iconColor = Color(0xFF159447),
                    onClick = {
                        navController.navigate(AdminBottomScreen.Products.route)
                    }
                )
            }

            item {
                QuickActionItem(
                    title = "Categories",
                    icon = Icons.Default.Category,
                    background = Color(0xFFE7F0FF),
                    iconColor = Color(0xFF1479F2),
                    onClick = {
                        navController.navigate(Routes.Categories)
                    }
                )
            }

            item {
                QuickActionItem(
                    title = "Posters",
                    icon = Icons.Default.Image,
                    background = Color(0xFFF0EAFE),
                    iconColor = Color(0xFF7B4DD8),
                    onClick = {
                        navController.navigate(Routes.Poster)
                    }
                )
            }

            item {
                QuickActionItem(
                    title = "Orders",
                    icon = Icons.Default.LocalShipping,
                    background = Color(0xFFFFF3DF),
                    iconColor = Color(0xFFE98A00),
                    onClick = {
                        navController.navigate(AdminBottomScreen.Orders.route)
                    }
                )
            }

            item {
                QuickActionItem(
                    title = "Sales",
                    icon = Icons.Default.PointOfSale,
                    background = Color(0xFFE7F0FF),
                    iconColor = Color(0xFF1479F2),
                    onClick = {
                        navController.navigate(AdminBottomScreen.Sales.route)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        SectionTitle(
            title = "Recent Sales",
            subtitle = "Latest transactions"
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (uiState.sales.isEmpty()) {
            EmptySalesCard()
        } else {
            uiState.sales
                .takeLast(5)
                .reversed()
                .forEach { sale ->
                    RecentSaleCard(sale = sale)
                    Spacer(modifier = Modifier.height(10.dp))
                }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun WelcomeBanner(
    totalUsers: Int,
    totalProducts: Int,
    totalOrders: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF1479F2))
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Hello, Admin 👋",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Here's what's happening in your medical shop today.",
                color = Color.White.copy(alpha = 0.88f),
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                MiniBannerStat(
                    value = totalUsers.toString(),
                    label = "Users"
                )

                MiniBannerStat(
                    value = totalProducts.toString(),
                    label = "Products"
                )

                MiniBannerStat(
                    value = totalOrders.toString(),
                    label = "Orders"
                )
            }
        }
    }
}

@Composable
private fun MiniBannerStat(
    value: String,
    label: String
) {
    Column {
        Text(
            text = value,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = label,
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 11.sp
        )
    }
}

@Composable
private fun SectionTitle(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF123B78)
        )

        Text(
            text = subtitle,
            fontSize = 11.sp,
            color = Color(0xFF71839C)
        )
    }
}

@Composable
private fun DashboardStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconBackground: Color,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                color = Color(0xFF71839C),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                fontSize = 20.sp,
                color = Color(0xFF123B78),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = iconColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun AttentionCard(
    title: String,
    value: String,
    description: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF123B78)
                )

                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = Color(0xFF71839C)
                )
            }

            Text(
                text = value,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = iconColor
            )
        }
    }
}

@Composable
private fun UserStatusCard(
    approved: Int,
    pending: Int,
    blocked: Int,
    total: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            StatusRow(
                title = "Approved Users",
                value = approved,
                total = total,
                color = Color(0xFF159447),
                icon = Icons.Default.CheckCircle
            )

            Spacer(modifier = Modifier.height(14.dp))

            StatusRow(
                title = "Pending Users",
                value = pending,
                total = total,
                color = Color(0xFFE98A00),
                icon = Icons.Default.Assignment
            )

            Spacer(modifier = Modifier.height(14.dp))

            StatusRow(
                title = "Blocked Users",
                value = blocked,
                total = total,
                color = Color(0xFFD93A3A),
                icon = Icons.Default.Block
            )
        }
    }
}

@Composable
private fun OrderStatusCard(
    pending: Int,
    approved: Int,
    completed: Int,
    total: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            StatusRow(
                title = "Pending Orders",
                value = pending,
                total = total,
                color = Color(0xFFE98A00),
                icon = Icons.Default.Assignment
            )

            Spacer(modifier = Modifier.height(14.dp))

            StatusRow(
                title = "Approved Orders",
                value = approved,
                total = total,
                color = Color(0xFF1479F2),
                icon = Icons.Default.CheckCircle
            )

            Spacer(modifier = Modifier.height(14.dp))

            StatusRow(
                title = "Completed Orders",
                value = completed,
                total = total,
                color = Color(0xFF159447),
                icon = Icons.Default.LocalShipping
            )
        }
    }
}

@Composable
private fun StatusRow(
    title: String,
    value: Int,
    total: Int,
    color: Color,
    icon: ImageVector
) {
    val progress = remember(value, total) {
        if (total > 0) {
            (value.toFloat() / total.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(19.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF45617F)
                )

                Text(
                    text = value.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFEAF0F6))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color)
                )
            }
        }
    }
}

@Composable
private fun QuickActionItem(
    title: String,
    icon: ImageVector,
    background: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(88.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 14.dp,
                horizontal = 8.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF123B78),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun RecentSaleCard(sale: Sale) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(Color(0xFFE7F0FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PointOfSale,
                    contentDescription = null,
                    tint = Color(0xFF1479F2),
                    modifier = Modifier.size(21.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sale.product_name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF123B78),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${sale.user_name} • Qty ${sale.quantity}",
                    fontSize = 10.sp,
                    color = Color(0xFF71839C),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = formatSaleDate(sale.date_of_sell),
                    fontSize = 10.sp,
                    color = Color(0xFF9AA9BB)
                )
            }

            Text(
                text = "₹${formatAmount(sale.total_amount)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF159447)
            )
        }
    }
}

@Composable
private fun EmptySalesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.PointOfSale,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFF9AA9BB)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "No sales yet",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF45617F)
            )

            Text(
                text = "Recent transactions will appear here.",
                fontSize = 11.sp,
                color = Color(0xFF9AA9BB)
            )
        }
    }
}

@Composable
private fun DashboardLoading(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Color(0xFF1479F2))

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Loading dashboard...",
                color = Color(0xFF71839C),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun DashboardError(
    paddingValues: PaddingValues,
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = Color(0xFFD93A3A)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Unable to load dashboard",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF123B78)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = message,
            fontSize = 12.sp,
            color = Color(0xFF71839C)
        )

        Spacer(modifier = Modifier.height(14.dp))

        IconButton(onClick = onRetry) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Retry",
                tint = Color(0xFF1479F2)
            )
        }

        Text(
            text = "Tap refresh to try again",
            fontSize = 11.sp,
            color = Color(0xFF71839C)
        )
    }
}

private fun formatAmount(amount: Double): String {
    return String.format(
        Locale.getDefault(),
        "%.2f",
        amount
    )
}

private fun formatSaleDate(date: String): String {
    if (date.isBlank()) {
        return "Date unavailable"
    }

    val formats = listOf(
        "yyyy-MM-dd",
        "yyyy/MM/dd",
        "dd-MM-yyyy",
        "dd/MM/yyyy",
        "dd MMM yyyy",
        "dd MMMM yyyy"
    )

    val outputFormat = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.ENGLISH
    )

    for (format in formats) {
        try {
            val inputFormat = SimpleDateFormat(
                format,
                Locale.ENGLISH
            )

            inputFormat.isLenient = false

            val parsed = inputFormat.parse(date.trim())

            if (parsed != null) {
                return outputFormat.format(parsed)
            }
        } catch (_: Exception) {
        }
    }

    return date
}