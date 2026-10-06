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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.medicineadmin.model.User
import com.example.medicineadmin.view.components.AdminBottomBar
import com.example.medicineadmin.view.components.AdminTopBar
import com.example.medicineadmin.view.navigation.Routes
import com.example.medicineadmin.viewModel.UsersViewModel

data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val status: String
)

@Composable
fun UsersScreen(
    navController: NavController,
    viewModel: UsersViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    val tabs = listOf(
        "All",
        "Pending",
        "Approved",
        "Blocked"
    )

    // ============================================================
    // API USER -> UI USER
    // ============================================================

    val usersList = uiState.users.map { user ->

        UserModel(
            id = user.user_id,
            name = user.name,
            email = user.email,
            phone = user.phone_number,
            status = getUserStatus(user)
        )
    }

    // ============================================================
    // FILTER USERS
    //
    // All      -> Pending + Approved
    // Pending  -> Pending
    // Approved -> Approved
    // Blocked  -> Blocked
    // ============================================================

    val filteredUsers = usersList.filter { user ->

        val matchesTab = when (tabs[selectedTab]) {

            // Blocked users All tab me nahi dikhenge
            "All" -> user.status != "Blocked"

            "Pending" -> user.status == "Pending"

            "Approved" -> user.status == "Approved"

            "Blocked" -> user.status == "Blocked"

            else -> false
        }

        val matchesSearch =
            user.name.contains(
                searchQuery,
                ignoreCase = true
            ) ||
                    user.email.contains(
                        searchQuery,
                        ignoreCase = true
                    ) ||
                    user.id.contains(
                        searchQuery,
                        ignoreCase = true
                    )

        matchesTab && matchesSearch
    }

    // ============================================================
    // SCREEN
    // ============================================================

    Scaffold(

        // ========================================================
        // FIXED TOP BAR
        // Existing AdminTopBar is used here.
        // ========================================================

        topBar = {

            AdminTopBar(

                onMenuClick = {
                    // Menu action
                },

                onNotificationClick = {
                    // Notification action
                },

                onProfileClick = {
                    // Profile action
                }
            )
        },

        // ========================================================
        // BOTTOM BAR
        // ========================================================

        bottomBar = {

            AdminBottomBar(
                navController = navController,
                currentRoute = Routes.Users
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
            // SEARCH
            // ====================================================

            OutlinedTextField(

                value = searchQuery,

                onValueChange = {
                    searchQuery = it
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),

                singleLine = true,

                placeholder = {
                    Text(
                        text = "Search by name, email or ID..."
                    )
                },

                leadingIcon = {

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF1479F2)
                    )
                },

                shape = RoundedCornerShape(14.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1479F2),
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            // ====================================================
            // TABS
            // ====================================================

            ScrollableTabRow(

                selectedTabIndex = selectedTab,

                edgePadding = 16.dp,

                containerColor = Color.Transparent,

                divider = {}
            ) {

                tabs.forEachIndexed { index, title ->

                    Tab(

                        selected = selectedTab == index,

                        onClick = {
                            selectedTab = index
                        },

                        text = {

                            Text(

                                text = title,

                                fontWeight =
                                    if (selectedTab == index) {
                                        FontWeight.Bold
                                    } else {
                                        FontWeight.Medium
                                    }
                            )
                        },

                        selectedContentColor =
                            Color(0xFF1479F2),

                        unselectedContentColor =
                            Color(0xFF64748B)
                    )
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

                    contentAlignment =
                        Alignment.Center
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
                uiState.users.isEmpty()
            ) {

                Column(

                    modifier = Modifier.fillMaxSize(),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Warning,

                        contentDescription = null,

                        modifier =
                            Modifier.size(56.dp),

                        tint =
                            Color(0xFFE53E3E)
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(

                        text =
                            uiState.error
                                ?: "Something went wrong.",

                        style =
                            MaterialTheme.typography.titleMedium,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Button(

                        onClick = {
                            viewModel.refreshUsers()
                        },

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFF1479F2)
                            )
                    ) {

                        Text("Retry")
                    }
                }

            }

            // ====================================================
            // EMPTY
            // ====================================================

            else if (filteredUsers.isEmpty()) {

                Column(

                    modifier = Modifier.fillMaxSize(),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Person,

                        contentDescription = null,

                        modifier =
                            Modifier.size(56.dp),

                        tint =
                            Color(0xFF94A3B8)
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(

                        text = "No users found",

                        style =
                            MaterialTheme.typography.titleMedium,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(

                        text =
                            "Try another search or filter.",

                        style =
                            MaterialTheme.typography.bodyMedium,

                        color =
                            Color(0xFF64748B)
                    )
                }

            }

            // ====================================================
            // USER LIST
            // ====================================================

            else {

                LazyColumn(

                    modifier =
                        Modifier.fillMaxSize(),

                    verticalArrangement =
                        Arrangement.spacedBy(12.dp),

                    contentPadding =
                        PaddingValues(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                ) {

                    items(

                        items = filteredUsers,

                        key = {
                            it.id
                        }

                    ) { user ->

                        UserCard(

                            user = user,

                            // ==================================================
                            // STATUS ACTION
                            // ==================================================

                            onStatusChange = { newStatus ->

                                when (newStatus) {

                                    // ==========================================
                                    // APPROVE
                                    // ==========================================

                                    "Approved" -> {

                                        viewModel.approveUser(
                                            userId = user.id
                                        )
                                    }

                                    // ==========================================
                                    // BLOCK
                                    // ==========================================

                                    "Blocked" -> {

                                        viewModel.blockUser(
                                            userId = user.id
                                        )
                                    }

                                    // ==========================================
                                    // UNBLOCK
                                    // ==========================================

                                    "Unblocked" -> {

                                        viewModel.unblockUser(
                                            userId = user.id
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


// ================================================================
// GET USER STATUS
// ================================================================

private fun getUserStatus(
    user: User
): String {

    return when {

        user.block == 1 ->
            "Blocked"

        user.isApproved == 1 ->
            "Approved"

        else ->
            "Pending"
    }
}


// ================================================================
// USER CARD
// ================================================================

@Composable
private fun UserCard(
    user: UserModel,
    onStatusChange: (String) -> Unit
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(16.dp),

        colors =
            CardDefaults.cardColors(
                containerColor = Color.White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            // ====================================================
            // USER HEADER
            // ====================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(

                    modifier = Modifier
                        .size(45.dp)
                        .background(
                            Color(0xFFEBF3FF),
                            RoundedCornerShape(12.dp)
                        ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Person,

                        contentDescription = null,

                        tint =
                            Color(0xFF1479F2),

                        modifier =
                            Modifier.size(24.dp)
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(12.dp)
                )

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(

                        text = user.name,

                        style =
                            MaterialTheme.typography.titleMedium,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF123B78)
                    )

                    Text(

                        text = user.id,

                        style =
                            MaterialTheme.typography.bodySmall,

                        color =
                            Color(0xFF64748B)
                    )
                }

                UserStatusChip(
                    status = user.status
                )
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            // ====================================================
            // EMAIL
            // ====================================================

            Text(

                text = user.email,

                style =
                    MaterialTheme.typography.bodyMedium,

                color =
                    Color(0xFF334155)
            )

            Spacer(
                modifier =
                    Modifier.height(2.dp)
            )

            // ====================================================
            // PHONE
            // ====================================================

            Text(

                text = user.phone,

                style =
                    MaterialTheme.typography.bodyMedium,

                color =
                    Color(0xFF64748B)
            )

            // ====================================================
            // ACTION BUTTONS
            // ====================================================

            when (user.status) {

                // ==================================================
                // PENDING -> APPROVE
                // ==================================================

                "Pending" -> {

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.End
                    ) {

                        Button(

                            onClick = {
                                onStatusChange("Approved")
                            },

                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        Color(0xFF20A849)
                                ),

                            shape =
                                RoundedCornerShape(8.dp),

                            contentPadding =
                                PaddingValues(
                                    horizontal = 12.dp,
                                    vertical = 4.dp
                                )
                        ) {

                            Icon(

                                imageVector =
                                    Icons.Default.CheckCircle,

                                contentDescription = null,

                                modifier =
                                    Modifier.size(16.dp)
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(4.dp)
                            )

                            Text(
                                text = "Approve",
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // ==================================================
                // APPROVED -> BLOCK
                // ==================================================

                "Approved" -> {

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.End
                    ) {

                        OutlinedButton(

                            onClick = {
                                onStatusChange("Blocked")
                            },

                            shape =
                                RoundedCornerShape(8.dp),

                            contentPadding =
                                PaddingValues(
                                    horizontal = 12.dp,
                                    vertical = 4.dp
                                )
                        ) {

                            Icon(

                                imageVector =
                                    Icons.Default.Block,

                                contentDescription = null,

                                modifier =
                                    Modifier.size(16.dp),

                                tint =
                                    Color.Red
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(4.dp)
                            )

                            Text(

                                text = "Block",

                                fontSize = 12.sp,

                                color =
                                    Color.Red
                            )
                        }
                    }
                }

                // ==================================================
                // BLOCKED -> UNBLOCK
                // ==================================================

                "Blocked" -> {

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.End
                    ) {

                        Button(

                            onClick = {
                                onStatusChange("Unblocked")
                            },

                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        Color(0xFF20A849)
                                ),

                            shape =
                                RoundedCornerShape(8.dp),

                            contentPadding =
                                PaddingValues(
                                    horizontal = 12.dp,
                                    vertical = 4.dp
                                )
                        ) {

                            Icon(

                                imageVector =
                                    Icons.Default.CheckCircle,

                                contentDescription = null,

                                modifier =
                                    Modifier.size(16.dp)
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(4.dp)
                            )

                            Text(
                                text = "Unblock",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


// ================================================================
// STATUS CHIP
// ================================================================

@Composable
private fun UserStatusChip(
    status: String
) {

    val (bgColor, textColor, icon) = when (status) {

        "Approved" -> Triple(
            Color(0xFFEAFBF1),
            Color(0xFF20A849),
            Icons.Default.CheckCircle
        )

        "Blocked" -> Triple(
            Color(0xFFFFEEEE),
            Color(0xFFE53E3E),
            Icons.Default.Block
        )

        else -> Triple(
            Color(0xFFFFF7EB),
            Color(0xFFD97706),
            Icons.Default.Warning
        )
    }

    Surface(

        color = bgColor,

        shape =
            RoundedCornerShape(8.dp)
    ) {

        Row(

            modifier =
                Modifier.padding(
                    horizontal = 8.dp,
                    vertical = 4.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(

                imageVector = icon,

                contentDescription = null,

                tint = textColor,

                modifier =
                    Modifier.size(12.dp)
            )

            Spacer(
                modifier =
                    Modifier.width(4.dp)
            )

            Text(

                text = status,

                color = textColor,

                fontSize = 11.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}