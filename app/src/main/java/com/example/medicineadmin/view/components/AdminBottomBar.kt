package com.example.medicineadmin.view.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicineadmin.view.navigation.Routes

// Bottom Navigation Items
sealed class AdminBottomScreen(
    val route: Routes,
    val title: String,
    val icon: ImageVector
) {

    data object Home : AdminBottomScreen(
        Routes.Dashboard,
        "Home",
        Icons.Default.Home
    )

    data object Users : AdminBottomScreen(
        Routes.Users,
        "Users",
        Icons.Default.People
    )

    data object Products : AdminBottomScreen(
        Routes.Products,
        "Products",
        Icons.Default.Inventory2
    )

    data object Orders : AdminBottomScreen(
        Routes.Orders,
        "Orders",
        Icons.Default.ListAlt
    )

    data object Sales : AdminBottomScreen(
        Routes.Sales,
        "Sales",
        Icons.Default.BarChart
    )
}

@Composable
fun AdminBottomBar(
    navController: NavController,
    currentRoute: Routes?
) {

    val items = listOf(
        AdminBottomScreen.Home,
        AdminBottomScreen.Users,
        AdminBottomScreen.Products,
        AdminBottomScreen.Orders,
        AdminBottomScreen.Sales
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {

        items.forEach { screen ->

            val isSelected = currentRoute == screen.route

            NavigationBarItem(

                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                        modifier = Modifier.size(22.dp)
                    )
                },

                label = {
                    Text(
                        text = screen.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Medium
                        }
                    )
                },

                selected = isSelected,

                // ✅ UPDATED: Lightweight navigation
                onClick = {

                    if (currentRoute != screen.route) {

                        navController.navigate(screen.route) {

                            // ✅ Prevent duplicate destination
                            launchSingleTop = true
                        }
                    }
                },

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF1479F2),
                    selectedTextColor = Color(0xFF1479F2),

                    unselectedIconColor = Color(0xFF8A9FB8),
                    unselectedTextColor = Color(0xFF8A9FB8),

                    indicatorColor = Color(0xFFE2EEFE)
                )
            )
        }
    }
}