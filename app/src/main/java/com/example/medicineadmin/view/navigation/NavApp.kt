package com.example.medicineadmin.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medicineadmin.view.Notification.NotificationsScreen
import com.example.medicineadmin.view.Screen.CategoryScreen
import com.example.medicineadmin.view.Screen.DashboardScreen
import com.example.medicineadmin.view.Screen.OrdersScreen
import com.example.medicineadmin.view.Screen.PosterScreen
import com.example.medicineadmin.view.Screen.ProductsScreen
import com.example.medicineadmin.view.Screen.SalesScreen
import com.example.medicineadmin.view.Screen.SettingsScreen
import com.example.medicineadmin.view.Screen.UsersScreen

@Composable
fun NavApp() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Dashboard
    ) {

        composable<Routes.Dashboard> {
            DashboardScreen(navController = navController)
        }

        composable<Routes.Users> {
            UsersScreen(navController = navController)
        }

        composable<Routes.Products> {
            ProductsScreen(navController = navController)
        }

        composable<Routes.Categories> {
            CategoryScreen()
        }

        composable<Routes.Poster> {
            PosterScreen()
        }

        composable<Routes.Orders> {
            OrdersScreen(navController = navController)
        }

        composable<Routes.Sales> {
            SalesScreen(navController = navController)
        }

        composable<Routes.Settings> {
            SettingsScreen()
        }

        composable<Routes.Notifications> {
            NotificationsScreen(navController = navController)
        }
    }
}