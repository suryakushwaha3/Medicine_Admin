package com.example.medicineadmin.view.Screen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.medicineadmin.model.Poster
import com.example.medicineadmin.viewModel.PosterViewModel
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

private const val BASE_URL = "https://suryakush.pythonanywhere.com/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosterScreen(
    viewModel: PosterViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var selectedPosterId by remember {
        mutableStateOf<Int?>(null)
    }

    var selectedPosterName by remember {
        mutableStateOf("")
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    LaunchedEffect(uiState.message) {
        if (uiState.message != null) {
            showAddDialog = false
            selectedPosterId = null
            selectedPosterName = ""
            selectedImageUri = null
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Posters",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                        alpha = 0.5f
                    )
                ),
                actions = {
                    FilledTonalIconButton(
                        onClick = {
                            selectedPosterId = null
                            selectedPosterName = ""
                            selectedImageUri = null
                            viewModel.clearError()
                            showAddDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Poster"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            when {

                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.error != null && uiState.posters.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.error
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = uiState.error ?: "Something went wrong.",
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Button(
                            onClick = {
                                viewModel.getAllPosters()
                            }
                        ) {
                            Text("Retry")
                        }
                    }
                }

                uiState.posters.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Text(
                            text = "No Posters Available",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "Add banners and promotional posters for your store.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )

                        Button(
                            onClick = {
                                selectedPosterId = null
                                selectedPosterName = ""
                                selectedImageUri = null
                                viewModel.clearError()
                                showAddDialog = true
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            Text("Add First Poster")
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        items(
                            items = uiState.posters,
                            key = { it.id }
                        ) { poster ->

                            PosterCard(
                                poster = poster,
                                context = context,
                                onEdit = {
                                    selectedPosterId = poster.id
                                    selectedPosterName = poster.poster_name
                                    selectedImageUri = null
                                    viewModel.clearError()
                                    showAddDialog = true
                                },
                                onDelete = {
                                    selectedPosterId = poster.id
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // ---------------------------------------------------------
    // ADD / UPDATE POSTER DIALOG
    // ---------------------------------------------------------

    if (showAddDialog) {

        AlertDialog(
            onDismissRequest = {
                if (!uiState.isSaving) {
                    showAddDialog = false
                    selectedPosterId = null
                    selectedPosterName = ""
                    selectedImageUri = null
                    viewModel.clearError()
                }
            },
            title = {
                Text(
                    if (selectedPosterId == null) {
                        "Add New Poster"
                    } else {
                        "Update Poster"
                    }
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    OutlinedTextField(
                        value = selectedPosterName,
                        onValueChange = {
                            selectedPosterName = it
                            viewModel.clearError()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Poster Name")
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        enabled = !uiState.isSaving
                    )

                    OutlinedButton(
                        onClick = {
                            imagePicker.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !uiState.isSaving
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            if (selectedImageUri == null) {
                                "Choose Poster Image"
                            } else {
                                "Change Image"
                            }
                        )
                    }

                    selectedImageUri?.let { uri ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {

                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(uri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Poster Preview",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    if (uiState.error != null) {
                        Text(
                            text = uiState.error ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {

                Button(
                    onClick = {

                        if (selectedPosterName.isBlank()) {
                            return@Button
                        }

                        val nameBody = selectedPosterName
                            .trim()
                            .toRequestBody(
                                "text/plain".toMediaTypeOrNull()
                            )

                        val currentId = selectedPosterId

                        if (currentId == null) {

                            val uri = selectedImageUri
                                ?: return@Button

                            val imagePart = uriToMultipart(
                                context = context,
                                uri = uri,
                                fieldName = "poster_image"
                            )

                            if (imagePart != null) {

                                viewModel.addPoster(
                                    posterName = nameBody,
                                    posterImage = imagePart
                                )
                            }

                        } else {

                            val imagePart = selectedImageUri?.let { uri ->

                                uriToMultipart(
                                    context = context,
                                    uri = uri,
                                    fieldName = "poster_image"
                                )
                            }

                            viewModel.updatePoster(
                                posterId = currentId,
                                posterName = nameBody,
                                posterImage = imagePart
                            )
                        }
                    },
                    enabled = !uiState.isSaving &&
                            selectedPosterName.isNotBlank() &&
                            (
                                    selectedPosterId != null ||
                                            selectedImageUri != null
                                    )
                ) {

                    if (uiState.isSaving) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                    } else {

                        Text(
                            if (selectedPosterId == null) {
                                "Upload"
                            } else {
                                "Save Changes"
                            }
                        )
                    }
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        if (!uiState.isSaving) {
                            showAddDialog = false
                            selectedPosterId = null
                            selectedPosterName = ""
                            selectedImageUri = null
                            viewModel.clearError()
                        }
                    },
                    enabled = !uiState.isSaving
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // ---------------------------------------------------------
    // DELETE DIALOG
    // ---------------------------------------------------------

    if (showDeleteDialog) {

        AlertDialog(
            onDismissRequest = {
                if (!uiState.isSaving) {
                    showDeleteDialog = false
                    selectedPosterId = null
                }
            },
            title = {
                Text("Delete Poster")
            },
            text = {
                Text(
                    "Are you sure you want to remove this poster permanently?"
                )
            },
            confirmButton = {

                Button(
                    onClick = {

                        val currentId = selectedPosterId

                        if (currentId != null) {
                            viewModel.deletePoster(currentId)
                        }

                        showDeleteDialog = false
                        selectedPosterId = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    enabled = !uiState.isSaving
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        selectedPosterId = null
                    },
                    enabled = !uiState.isSaving
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

// =============================================================
// POSTER CARD
// =============================================================

@Composable
private fun PosterCard(
    poster: Poster,
    context: Context,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    val imageUrl = buildPosterImageUrl(
        poster.poster_image
    )

    // Debug log
    LaunchedEffect(imageUrl) {
        Log.d(
            "POSTER_IMAGE",
            "Poster: ${poster.poster_name}"
        )

        Log.d(
            "POSTER_IMAGE",
            "Image path: ${poster.poster_image}"
        )

        Log.d(
            "POSTER_IMAGE",
            "Final URL: $imageUrl"
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column {

            if (imageUrl.isNotBlank()) {

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = poster.poster_name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop,

                    onSuccess = {
                        Log.d(
                            "POSTER_IMAGE",
                            "Image loaded successfully: $imageUrl"
                        )
                    },

                    onError = { state ->
                        Log.e(
                            "POSTER_IMAGE",
                            "Image loading failed: $imageUrl",
                            state.result.throwable
                        )
                    }
                )

            } else {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = poster.poster_name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (poster.is_active == 1) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.errorContainer
                        }
                    ) {

                        Text(
                            text = if (poster.is_active == 1) {
                                " Active "
                            } else {
                                " Inactive "
                            },
                            modifier = Modifier.padding(
                                horizontal = 6.dp,
                                vertical = 2.dp
                            ),
                            color = if (poster.is_active == 1) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            },
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                IconButton(
                    onClick = onEdit
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = onDelete
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

// =============================================================
// BUILD POSTER IMAGE URL
// =============================================================

private fun buildPosterImageUrl(
    imagePath: String
): String {

    val cleanedPath = imagePath
        .trim()
        .replace("\\", "/")

    if (cleanedPath.isEmpty()) {
        return ""
    }

    return when {

        // API already returned complete URL
        cleanedPath.startsWith("http://") ||
                cleanedPath.startsWith("https://") -> {

            cleanedPath
        }

        // API returned:
        // uploads/posters/image.jpg
        cleanedPath.startsWith("uploads/") -> {

            BASE_URL + cleanedPath
        }

        // API returned only:
        // image.jpg
        else -> {

            BASE_URL +
                    "uploads/posters/" +
                    cleanedPath.removePrefix("/")
        }
    }
}

// =============================================================
// URI TO MULTIPART
// =============================================================

private fun uriToMultipart(
    context: Context,
    uri: Uri,
    fieldName: String
): MultipartBody.Part? {

    return try {

        val inputStream = context.contentResolver
            .openInputStream(uri)
            ?: return null

        val file = File(
            context.cacheDir,
            "poster_${System.currentTimeMillis()}.jpg"
        )

        inputStream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val requestBody = file.asRequestBody(
            "image/*".toMediaTypeOrNull()
        )

        MultipartBody.Part.createFormData(
            fieldName,
            file.name,
            requestBody
        )

    } catch (e: Exception) {

        Log.e(
            "POSTER_UPLOAD",
            "Failed to create multipart image",
            e
        )

        null
    }
}