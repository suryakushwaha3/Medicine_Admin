package com.example.medicineadmin.view.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class SettingItem(
    val title: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {

    var darkMode by rememberSaveable {
        mutableStateOf(false)
    }

    var notifications by rememberSaveable {
        mutableStateOf(true)
    }

    var autoUpdate by rememberSaveable {
        mutableStateOf(true)
    }

    val generalSettings = listOf(
        SettingItem(
            title = "Language",
            description = "Application language"
        ),
        SettingItem(
            title = "Notifications",
            description = "Receive admin notifications"
        ),
        SettingItem(
            title = "Dark Mode",
            description = "Use dark theme"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {

                        Text(
                            text = "Settings",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Admin panel settings",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                SettingsHeader(
                    title = "General",
                    icon = Icons.Default.Settings
                )
            }

            item {

                SettingsCard {

                    SettingRow(
                        icon = Icons.Default.Language,
                        title = "Language",
                        description = "English",
                        showSwitch = false
                    )

                    HorizontalDivider()

                    SettingRow(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        description = "Receive admin notifications",
                        checked = notifications,
                        onCheckedChange = {
                            notifications = it
                        }
                    )

                    HorizontalDivider()

                    SettingRow(
                        icon = Icons.Default.DarkMode,
                        title = "Dark Mode",
                        description = "Use dark theme",
                        checked = darkMode,
                        onCheckedChange = {
                            darkMode = it
                        }
                    )
                }
            }

            item {

                SettingsHeader(
                    title = "Application",
                    icon = Icons.Default.Api
                )
            }

            item {

                SettingsCard {

                    SettingRow(
                        icon = Icons.Default.Api,
                        title = "API Server",
                        description = "Flask backend connection"
                    )

                    HorizontalDivider()

                    SettingRow(
                        icon = Icons.Default.Storage,
                        title = "Database",
                        description = "SQLite database"
                    )

                    HorizontalDivider()

                    SettingRow(
                        icon = Icons.Default.Update,
                        title = "Auto Update",
                        description = "Automatically refresh data",
                        checked = autoUpdate,
                        onCheckedChange = {
                            autoUpdate = it
                        }
                    )
                }
            }

            item {

                SettingsHeader(
                    title = "About",
                    icon = Icons.Default.Info
                )
            }

            item {

                SettingsCard {

                    SettingRow(
                        icon = Icons.Default.Info,
                        title = "Application",
                        description = "Medicine Retail Admin Panel"
                    )

                    HorizontalDivider()

                    SettingRow(
                        icon = Icons.Default.Update,
                        title = "Version",
                        description = "1.0.0"
                    )
                }
            }

            item {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = "Medicine Retail Admin Panel",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Admin Dashboard",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }
        }
    }
}

@Composable
private fun SettingsHeader(
    title: String,
    icon: ImageVector
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.size(8.dp)
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SettingsCard(
    content: @Composable ColumnScope.() -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content
        )
    }
}

@Composable
private fun SettingRow(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean = false,
    showSwitch: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.size(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (showSwitch) {

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}