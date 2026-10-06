package com.example.medicineadmin.view.Notification


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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.medicineadmin.model.Notification
import com.example.medicineadmin.view.components.AdminBottomBar
import com.example.medicineadmin.view.components.AdminTopBar
import com.example.medicineadmin.view.navigation.Routes
import com.example.medicineadmin.viewModel.NotificationViewModel


@Composable
fun NotificationsScreen(
    navController: NavController,
    viewModel: NotificationViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // ============================================================
    // LOAD NOTIFICATIONS
    // ============================================================

    LaunchedEffect(Unit) {
        viewModel.refreshNotifications()
    }


    Scaffold(
        topBar = {
            AdminTopBar(
                onMenuClick = {
                    // Menu action
                },
                onNotificationClick = {
                    // Already on notification screen
                },
                onProfileClick = {
                    // Profile action
                }
            )
        },
        bottomBar = {
            AdminBottomBar(
                navController = navController,
                currentRoute = Routes.Dashboard
            )
        },
        containerColor = Color(0xFFF7F9FC)
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // ====================================================
            // HEADER
            // ====================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 14.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Notifications",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF123B78)
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = "${uiState.unreadCount} unread notifications",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }


                IconButton(
                    onClick = {
                        viewModel.refreshNotifications()
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color(0xFF1479F2)
                    )
                }
            }


            // ====================================================
            // ACTION BUTTONS
            // ====================================================

            if (uiState.notifications.isNotEmpty()) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp
                        ),
                    horizontalArrangement = Arrangement.End
                ) {

                    OutlinedButton(
                        onClick = {
                            viewModel.markAllNotificationsAsRead()
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = "Mark all read",
                            fontSize = 12.sp
                        )
                    }


                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )


                    OutlinedButton(
                        onClick = {
                            viewModel.deleteAllNotifications()
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFFE53E3E)
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text(
                            text = "Delete all",
                            fontSize = 12.sp,
                            color = Color(0xFFE53E3E)
                        )
                    }
                }
            }


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // ====================================================
            // LOADING
            // ====================================================

            if (uiState.isLoading) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator(
                        color = Color(0xFF1479F2)
                    )
                }

            }

            // ====================================================
            // ERROR
            // ====================================================

            else if (
                uiState.error != null &&
                uiState.notifications.isEmpty()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = Color(0xFFE53E3E)
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(
                        text = uiState.error
                            ?: "Something went wrong.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Button(
                        onClick = {
                            viewModel.refreshNotifications()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1479F2)
                        )
                    ) {

                        Text("Retry")
                    }
                }
            }

            // ====================================================
            // EMPTY STATE
            // ====================================================

            else if (uiState.notifications.isEmpty()) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                Color(0xFFEAF2FF)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            tint = Color(0xFF1479F2)
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "No notifications",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF123B78)
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "You're all caught up.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B)
                    )
                }
            }

            // ====================================================
            // NOTIFICATION LIST
            // ====================================================

            else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                ) {

                    items(
                        items = uiState.notifications,
                        key = {
                            it.id
                        }
                    ) { notification ->

                        NotificationCard(
                            notification = notification,
                            onRead = {
                                viewModel.markNotificationAsRead(
                                    notification.id
                                )
                            },
                            onDelete = {
                                viewModel.deleteNotification(
                                    notification.id
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}


// ================================================================
// NOTIFICATION CARD
// ================================================================

@Composable
private fun NotificationCard(
    notification: Notification,
    onRead: () -> Unit,
    onDelete: () -> Unit
) {

    val isUnread = notification.is_read == 0


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnread) {
                Color(0xFFF0F7FF)
            } else {
                Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {

                NotificationIcon(
                    type = notification.type
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )


                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = notification.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF123B78),
                            modifier = Modifier.weight(1f)
                        )

                        if (isUnread) {

                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Color(0xFF1479F2)
                                    )
                            )
                        }
                    }


                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )


                    Text(
                        text = notification.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF475569)
                    )


                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )


                    Text(
                        text = notification.created_at,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF94A3B8)
                    )
                }


                IconButton(
                    onClick = onDelete
                ) {

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFE53E3E),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }


            // ====================================================
            // MARK AS READ
            // ====================================================

            if (isUnread) {

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    OutlinedButton(
                        onClick = onRead,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(
                            horizontal = 10.dp,
                            vertical = 4.dp
                        )
                    ) {

                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF20A849)
                        )

                        Spacer(
                            modifier = Modifier.width(4.dp)
                        )

                        Text(
                            text = "Mark as read",
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}


// ================================================================
// NOTIFICATION ICON
// ================================================================

@Composable
private fun NotificationIcon(
    type: String
) {

    val iconData = when (type.uppercase()) {

        "USER_SIGNUP" -> Triple(
            Icons.Default.Notifications,
            Color(0xFFEAF2FF),
            Color(0xFF1479F2)
        )

        "WARNING" -> Triple(
            Icons.Default.Warning,
            Color(0xFFFFF7EB),
            Color(0xFFD97706)
        )

        "ERROR" -> Triple(
            Icons.Default.Error,
            Color(0xFFFFEEEE),
            Color(0xFFE53E3E)
        )

        else -> Triple(
            Icons.Default.Info,
            Color(0xFFEAFBF1),
            Color(0xFF20A849)
        )
    }


    Surface(
        modifier = Modifier.size(44.dp),
        shape = RoundedCornerShape(12.dp),
        color = iconData.second
    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = iconData.first,
                contentDescription = null,
                tint = iconData.third,
                modifier = Modifier.size(23.dp)
            )
        }
    }
}