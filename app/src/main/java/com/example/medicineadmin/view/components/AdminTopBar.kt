package com.example.medicineadmin.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminTopBar(
    onMenuClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit,
    notificationCount: Int = 0
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = Color.White,
        shadowElevation = 2.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // ====================================================
            // LEFT SIDE
            // ====================================================

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.size(40.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color(0xFF123B78),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Column {

                    Text(
                        text = "MediCare",
                        color = Color(0xFF1479F2),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 20.sp
                    )

                    Text(
                        text = "Admin Panel",
                        color = Color(0xFF5879A2),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }


            // ====================================================
            // RIGHT SIDE
            // ====================================================

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // =================================================
                // NOTIFICATION
                // =================================================

                Box {

                    IconButton(
                        onClick = onNotificationClick,
                        modifier = Modifier.size(40.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color(0xFF123B78),
                            modifier = Modifier.size(22.dp)
                        )
                    }


                    // =================================================
                    // DYNAMIC BADGE
                    // =================================================

                    if (notificationCount > 0) {

                        Badge(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(
                                    top = 6.dp,
                                    end = 6.dp
                                ),
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ) {

                            Text(
                                text = if (notificationCount > 99) {
                                    "99+"
                                } else {
                                    notificationCount.toString()
                                },
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }


                Spacer(
                    modifier = Modifier.width(4.dp)
                )


                // =================================================
                // PROFILE
                // =================================================

                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE3EEF9))
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color(0xFF1479F2),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}