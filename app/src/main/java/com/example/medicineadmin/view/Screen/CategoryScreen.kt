package com.example.medicineadmin.view.Screen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.medicineadmin.model.Category
import com.example.medicineadmin.viewModel.CategoryViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private const val BASE_URL =
    "https://suryakush.pythonanywhere.com/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()

    var showDialog by remember {
        mutableStateOf(false)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var selectedCategoryId by remember {
        mutableStateOf<Int?>(null)
    }

    var categoryName by remember {
        mutableStateOf("")
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->

            selectedImageUri = uri
        }

    // ============================================================
    // SUCCESS MESSAGE
    // ============================================================

    LaunchedEffect(uiState.message) {

        if (uiState.message != null) {

            showDialog = false

            selectedCategoryId = null

            categoryName = ""

            selectedImageUri = null

            viewModel.clearMessage()
        }
    }

    // ============================================================
    // SCREEN
    // ============================================================

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Categories")
                },

                actions = {

                    IconButton(

                        onClick = {

                            selectedCategoryId = null
                            categoryName = ""
                            selectedImageUri = null

                            viewModel.clearError()

                            showDialog = true
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Category"
                        )
                    }
                }
            )
        }

    ) { paddingValues ->

        when {

            // ====================================================
            // LOADING
            // ====================================================

            uiState.isLoading -> {

                Column(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    CircularProgressIndicator()
                }
            }

            // ====================================================
            // ERROR
            // ====================================================

            uiState.error != null &&
                    uiState.categories.isEmpty() -> {

                Column(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Text(
                        text =
                            uiState.error
                                ?: "Something went wrong.",

                        color =
                            MaterialTheme.colorScheme.error
                    )

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Button(
                        onClick = {
                            viewModel.getAllCategories()
                        }
                    ) {

                        Text("Retry")
                    }
                }
            }

            // ====================================================
            // EMPTY
            // ====================================================

            uiState.categories.isEmpty() -> {

                Column(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Category,

                        contentDescription = null,

                        modifier =
                            Modifier.size(64.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text =
                            "No categories found.",

                        style =
                            MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Button(

                        onClick = {

                            selectedCategoryId = null
                            categoryName = ""
                            selectedImageUri = null

                            viewModel.clearError()

                            showDialog = true
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Add,

                            contentDescription = null
                        )

                        Spacer(
                            modifier =
                                Modifier.size(8.dp)
                        )

                        Text("Add Category")
                    }
                }
            }

            // ====================================================
            // CATEGORY LIST
            // ====================================================

            else -> {

                LazyColumn(

                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),

                    contentPadding =
                        PaddingValues(16.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    items(

                        items =
                            uiState.categories,

                        key = {
                            it.id
                        }

                    ) { category ->

                        CategoryCard(

                            category =
                                category,

                            context =
                                context,

                            onEdit = {

                                selectedCategoryId =
                                    category.id

                                categoryName =
                                    category.category_name

                                selectedImageUri =
                                    null

                                viewModel.clearError()

                                showDialog =
                                    true
                            },

                            onDelete = {

                                selectedCategoryId =
                                    category.id

                                showDeleteDialog =
                                    true
                            }
                        )
                    }
                }
            }
        }
    }

    // ============================================================
    // ADD / EDIT CATEGORY DIALOG
    // ============================================================

    if (showDialog) {

        AlertDialog(

            onDismissRequest = {

                if (!uiState.isSaving) {

                    showDialog = false

                    selectedCategoryId = null

                    categoryName = ""

                    selectedImageUri = null

                    viewModel.clearError()
                }
            },

            title = {

                Text(
                    if (selectedCategoryId == null) {
                        "Add Category"
                    } else {
                        "Edit Category"
                    }
                )
            },

            text = {

                Column(

                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    // =================================================
                    // CATEGORY NAME
                    // =================================================

                    OutlinedTextField(

                        value =
                            categoryName,

                        onValueChange = {

                            categoryName = it

                            viewModel.clearError()
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        label = {
                            Text("Category Name")
                        },

                        singleLine = true,

                        enabled =
                            !uiState.isSaving
                    )

                    // =================================================
                    // IMAGE PICKER
                    // =================================================

                    Button(

                        onClick = {

                            imagePicker.launch(
                                "image/*"
                            )
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        enabled =
                            !uiState.isSaving
                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Category,

                            contentDescription = null
                        )

                        Spacer(
                            modifier =
                                Modifier.size(8.dp)
                        )

                        Text(

                            if (selectedImageUri == null) {
                                "Select Category Image"
                            } else {
                                "Change Image"
                            }
                        )
                    }

                    // =================================================
                    // SELECTED IMAGE PREVIEW
                    // =================================================

                    selectedImageUri?.let { uri ->

                        AsyncImage(

                            model =
                                ImageRequest.Builder(context)
                                    .data(uri)
                                    .crossfade(true)
                                    .build(),

                            contentDescription =
                                "Selected category image",

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(
                                        RoundedCornerShape(12.dp)
                                    ),

                            contentScale =
                                ContentScale.Crop
                        )
                    }

                    // =================================================
                    // ERROR
                    // =================================================

                    if (uiState.error != null) {

                        Text(

                            text =
                                uiState.error ?: "",

                            color =
                                MaterialTheme.colorScheme.error,

                            style =
                                MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },

            // ========================================================
            // CONFIRM
            // ========================================================

            confirmButton = {

                Button(

                    onClick = {

                        if (categoryName.isBlank()) {
                            return@Button
                        }

                        val nameBody =
                            categoryName
                                .trim()
                                .toRequestBody(
                                    "text/plain"
                                        .toMediaTypeOrNull()
                                )

                        // =================================================
                        // ADD
                        // =================================================

                        if (selectedCategoryId == null) {

                            val uri =
                                selectedImageUri
                                    ?: return@Button

                            val imagePart =
                                uriToCategoryMultipart(
                                    context = context,
                                    uri = uri,
                                    fieldName = "category_image"
                                )

                            if (imagePart != null) {

                                viewModel.addCategory(

                                    categoryName =
                                        nameBody,

                                    categoryImage =
                                        imagePart
                                )
                            }
                        }

                        // =================================================
                        // UPDATE
                        // =================================================

                        else {

                            val imagePart =
                                selectedImageUri?.let { uri ->

                                    uriToCategoryMultipart(
                                        context = context,
                                        uri = uri,
                                        fieldName = "category_image"
                                    )
                                }

                            viewModel.updateCategory(

                                categoryId =
                                    selectedCategoryId!!,

                                categoryName =
                                    nameBody,

                                categoryImage =
                                    imagePart
                            )
                        }
                    },

                    enabled =
                        !uiState.isSaving &&
                                categoryName.isNotBlank() &&
                                (
                                        selectedCategoryId != null ||
                                                selectedImageUri != null
                                        )
                ) {

                    if (uiState.isSaving) {

                        CircularProgressIndicator(

                            modifier =
                                Modifier.size(18.dp),

                            strokeWidth =
                                2.dp
                        )

                    } else {

                        Text(

                            if (selectedCategoryId == null) {
                                "Add"
                            } else {
                                "Update"
                            }
                        )
                    }
                }
            },

            // ========================================================
            // CANCEL
            // ========================================================

            dismissButton = {

                Button(

                    onClick = {

                        if (!uiState.isSaving) {

                            showDialog = false

                            selectedCategoryId = null

                            categoryName = ""

                            selectedImageUri = null

                            viewModel.clearError()
                        }
                    },

                    enabled =
                        !uiState.isSaving
                ) {

                    Text("Cancel")
                }
            }
        )
    }

    // ============================================================
    // DELETE DIALOG
    // ============================================================

    if (showDeleteDialog) {

        AlertDialog(

            onDismissRequest = {

                if (!uiState.isSaving) {

                    showDeleteDialog = false
                    selectedCategoryId = null
                }
            },

            title = {

                Text("Delete Category")
            },

            text = {

                Text(
                    "Are you sure you want to delete this category?"
                )
            },

            confirmButton = {

                Button(

                    onClick = {

                        selectedCategoryId?.let { id ->

                            viewModel.deleteCategory(id)
                        }

                        showDeleteDialog = false
                        selectedCategoryId = null
                    },

                    enabled =
                        !uiState.isSaving
                ) {

                    Text("Delete")
                }
            },

            dismissButton = {

                Button(

                    onClick = {

                        showDeleteDialog = false
                        selectedCategoryId = null
                    },

                    enabled =
                        !uiState.isSaving
                ) {

                    Text("Cancel")
                }
            }
        )
    }
}


// =================================================================
// CATEGORY CARD
// =================================================================

@Composable
private fun CategoryCard(

    category: Category,

    context: Context,

    onEdit: () -> Unit,

    onDelete: () -> Unit

) {

    val imageUrl =
        remember(category.category_image) {

            buildCategoryImageUrl(
                category.category_image
            )
        }

    // ============================================================
    // DEBUG
    // ============================================================

    LaunchedEffect(imageUrl) {

        Log.d(
            "CATEGORY_IMAGE",
            "================================"
        )

        Log.d(
            "CATEGORY_IMAGE",
            "Category: ${category.category_name}"
        )

        Log.d(
            "CATEGORY_IMAGE",
            "API Image: ${category.category_image}"
        )

        Log.d(
            "CATEGORY_IMAGE",
            "Final URL: $imageUrl"
        )

        Log.d(
            "CATEGORY_IMAGE",
            "================================"
        )
    }

    Card(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column {

            // ======================================================
            // CATEGORY IMAGE
            // ======================================================

            AsyncImage(

                model =
                    ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),

                contentDescription =
                    category.category_name,

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(160.dp),

                contentScale =
                    ContentScale.Crop,

                onSuccess = {

                    Log.d(
                        "CATEGORY_IMAGE",
                        "IMAGE SUCCESS"
                    )

                    Log.d(
                        "CATEGORY_IMAGE",
                        "Loaded URL: $imageUrl"
                    )
                },

                onError = { state ->

                    Log.e(
                        "CATEGORY_IMAGE",
                        "IMAGE ERROR"
                    )

                    Log.e(
                        "CATEGORY_IMAGE",
                        "Failed URL: $imageUrl"
                    )

                    Log.e(
                        "CATEGORY_IMAGE",
                        "Error: ${state.result.throwable}"
                    )
                }
            )

            // ======================================================
            // CATEGORY DETAILS
            // ======================================================

            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(

                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(

                        text =
                            category.category_name,

                        style =
                            MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    val isActive =
                        category.is_active == 1

                    Text(

                        text =
                            if (isActive) {
                                "Active"
                            } else {
                                "Inactive"
                            },

                        color =
                            if (isActive) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            },

                        style =
                            MaterialTheme.typography.bodyMedium
                    )
                }

                IconButton(
                    onClick = onEdit
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Edit,

                        contentDescription =
                            "Edit Category"
                    )
                }

                IconButton(
                    onClick = onDelete
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Delete,

                        contentDescription =
                            "Delete Category"
                    )
                }
            }
        }
    }
}


// =================================================================
// BUILD CATEGORY IMAGE URL
// =================================================================

private fun buildCategoryImageUrl(
    imagePath: String
): String {

    var cleanedPath =
        imagePath
            .trim()
            .replace("\\", "/")

    if (cleanedPath.isEmpty()) {
        return ""
    }

    // ------------------------------------------------------------
    // Already complete URL
    // ------------------------------------------------------------

    if (
        cleanedPath.startsWith("http://") ||
        cleanedPath.startsWith("https://")
    ) {

        return cleanedPath
    }

    // ------------------------------------------------------------
    // Remove leading slash
    // ------------------------------------------------------------

    cleanedPath =
        cleanedPath.removePrefix("/")

    // ------------------------------------------------------------
    // If backend already returned uploads/categories/...
    // ------------------------------------------------------------

    if (
        cleanedPath.startsWith(
            "uploads/categories/",
            ignoreCase = true
        )
    ) {

        return BASE_URL + cleanedPath
    }

    // ------------------------------------------------------------
    // If backend returned categories/filename
    // ------------------------------------------------------------

    if (
        cleanedPath.startsWith(
            "categories/",
            ignoreCase = true
        )
    ) {

        return BASE_URL +
                "uploads/" +
                cleanedPath
    }

    // ------------------------------------------------------------
    // Only filename returned
    // ------------------------------------------------------------

    return BASE_URL +
            "uploads/categories/" +
            cleanedPath
}


// =================================================================
// URI → MULTIPART
// =================================================================

private fun uriToCategoryMultipart(

    context: Context,

    uri: Uri,

    fieldName: String

): MultipartBody.Part? {

    return try {

        val inputStream =
            context.contentResolver
                .openInputStream(uri)
                ?: return null

        val file =
            File(
                context.cacheDir,
                "category_${System.currentTimeMillis()}.jpg"
            )

        inputStream.use { input ->

            file.outputStream().use { output ->

                input.copyTo(output)
            }
        }

        val requestBody =
            file.asRequestBody(
                "image/*".toMediaTypeOrNull()
            )

        MultipartBody.Part.createFormData(

            fieldName,

            file.name,

            requestBody
        )

    } catch (e: Exception) {

        Log.e(
            "CATEGORY_UPLOAD",
            "Failed to create category multipart",
            e
        )

        null
    }
}