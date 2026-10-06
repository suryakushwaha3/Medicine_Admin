package com.example.medicineadmin

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.example.medicineadmin.ui.theme.MedicineAdminTheme
import com.example.medicineadmin.view.navigation.NavApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Android 13+ notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                ),
                1001
            )
        }

        setContent {
            MedicineAdminTheme {
                NavApp()
            }
        }
    }
}