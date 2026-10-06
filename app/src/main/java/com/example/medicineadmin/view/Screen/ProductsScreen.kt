package com.example.medicineadmin.view.Screen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.medicineadmin.model.Product
import com.example.medicineadmin.view.components.AdminBottomBar
import com.example.medicineadmin.view.navigation.Routes
import com.example.medicineadmin.viewModel.ProductsViewModel

private const val BASE_URL =
    "https://suryakush.pythonanywhere.com/"

private fun getProductImageUrl(imagePath: String?): String? {
    if (imagePath.isNullOrBlank()) return null

    return if (
        imagePath.startsWith("http://") ||
        imagePath.startsWith("https://")
    ) {
        imagePath
    } else {
        BASE_URL + imagePath.removePrefix("/")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    navController: NavController,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var searchQuery by remember {
        mutableStateOf("")
    }

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var selectedProduct by remember {
        mutableStateOf<Product?>(null)
    }

    val filteredProducts = uiState.products.filter { product ->
        product.product_name.contains(searchQuery, ignoreCase = true) ||
                product.category.contains(searchQuery, ignoreCase = true) ||
                product.product_id.contains(searchQuery, ignoreCase = true)
    }

    LaunchedEffect(uiState.successMessage) {
        if (
            uiState.successMessage != null &&
            uiState.isUpdating.not()
        ) {
            selectedProduct = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Products",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Manage store products",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            )
        },

        bottomBar = {
            AdminBottomBar(
                navController = navController,
                currentRoute = Routes.Products
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Product"
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background
                )
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text("Search products...")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "${filteredProducts.size} Products",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            when {

                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null &&
                        uiState.products.isEmpty() -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.error
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = uiState.error
                                ?: "Something went wrong",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        TextButton(
                            onClick = {
                                viewModel.refreshProducts()
                            }
                        ) {
                            Text("Retry")
                        }
                    }
                }

                filteredProducts.isEmpty() -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "No products found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = if (searchQuery.isBlank()) {
                                "No products available."
                            } else {
                                "Try another search."
                            },
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 90.dp)
                    ) {

                        items(
                            items = filteredProducts,
                            key = {
                                it.id
                            }
                        ) { product ->

                            ProductCard(
                                product = product,

                                onEdit = {
                                    selectedProduct = product
                                },

                                onDelete = {
                                    viewModel.deleteProduct(
                                        product.product_id
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {

        ProductDialog(
            title = "Add Product",

            isSubmitting = uiState.isAdding,

            error = uiState.error,

            onDismiss = {
                if (!uiState.isAdding) {
                    showAddDialog = false
                    viewModel.clearError()
                }
            },

            onSubmit = {
                    productId,
                    name,
                    category,
                    stock,
                    imageUri ->

                if (imageUri != null) {

                    viewModel.addProduct(
                        productId = productId,
                        productName = name,
                        category = category,
                        stock = stock,
                        imageUri = imageUri
                    )

                    showAddDialog = false
                }
            }
        )
    }

    selectedProduct?.let { product ->

        ProductDialog(
            title = "Edit Product",

            product = product,

            isSubmitting = uiState.isUpdating,

            error = uiState.error,

            onDismiss = {
                if (!uiState.isUpdating) {
                    selectedProduct = null
                    viewModel.clearError()
                }
            },

            onSubmit = {
                    productId,
                    name,
                    category,
                    stock,
                    imageUri ->

                viewModel.updateProduct(
                    productId = productId,
                    productName = name,
                    category = category,
                    stock = stock,
                    imageUri = imageUri
                )
            }
        )
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    val imageUrl = getProductImageUrl(
        product.product_image
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(
                        RoundedCornerShape(14.dp)
                    )
                    .background(
                        MaterialTheme.colorScheme.primaryContainer
                    ),
                contentAlignment = Alignment.Center
            ) {

                if (imageUrl != null) {

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = product.product_name,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(
                                RoundedCornerShape(14.dp)
                            ),
                        contentScale = ContentScale.Crop,

                        onSuccess = {
                            Log.d(
                                "PRODUCT_IMAGE",
                                "Loaded: $imageUrl"
                            )
                        },

                        onError = { state ->
                            Log.e(
                                "PRODUCT_IMAGE",
                                "Failed: $imageUrl",
                                state.result.throwable
                            )
                        }
                    )

                } else {

                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = "No product image",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(
                modifier = Modifier.size(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = product.product_name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = product.product_id,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = product.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "Stock: ${product.stock}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row {

                IconButton(
                    onClick = onEdit
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Product"
                    )
                }

                IconButton(
                    onClick = onDelete
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Product",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductDialog(
    title: String,
    product: Product? = null,
    isSubmitting: Boolean = false,
    error: String? = null,
    onDismiss: () -> Unit,
    onSubmit: (
        productId: String,
        name: String,
        category: String,
        stock: Int,
        imageUri: Uri?
    ) -> Unit
) {

    var productId by remember {
        mutableStateOf(
            product?.product_id ?: ""
        )
    }

    var name by remember {
        mutableStateOf(
            product?.product_name ?: ""
        )
    }

    var category by remember {
        mutableStateOf(
            product?.category ?: ""
        )
    }

    var stock by remember {
        mutableStateOf(
            product?.stock?.toString() ?: ""
        )
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            imageUri = uri
        }

    AlertDialog(
        onDismissRequest = {
            if (!isSubmitting) {
                onDismiss()
            }
        },

        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    when {

                        imageUri != null -> {

                            AsyncImage(
                                model = imageUri,
                                contentDescription = "Selected product image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(
                                        RoundedCornerShape(16.dp)
                                    ),
                                contentScale = ContentScale.Crop
                            )
                        }

                        !product?.product_image.isNullOrBlank() -> {

                            val existingImageUrl =
                                getProductImageUrl(
                                    product?.product_image
                                )

                            AsyncImage(
                                model = existingImageUrl,
                                contentDescription = product?.product_name,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(
                                        RoundedCornerShape(16.dp)
                                    ),
                                contentScale = ContentScale.Crop,

                                onError = { state ->
                                    Log.e(
                                        "PRODUCT_IMAGE",
                                        "Edit image failed: $existingImageUrl",
                                        state.result.throwable
                                    )
                                }
                            )
                        }

                        else -> {

                            Column(
                                horizontalAlignment =
                                    Alignment.CenterHorizontally
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(
                                    modifier = Modifier.height(6.dp)
                                )

                                Text(
                                    text = "No image selected",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        imagePickerLauncher.launch("image/*")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSubmitting
                ) {

                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.size(8.dp)
                    )

                    Text(
                        text = if (imageUri == null) {
                            if (product == null) {
                                "Select Product Image"
                            } else {
                                "Change Product Image"
                            }
                        } else {
                            "Change Product Image"
                        }
                    )
                }

                OutlinedTextField(
                    value = productId,
                    onValueChange = {
                        productId = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("Product ID")
                    },
                    enabled = product == null && !isSubmitting
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("Product Name")
                    },
                    enabled = !isSubmitting
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = {
                        category = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("Category")
                    },
                    enabled = !isSubmitting
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = {
                        if (
                            it.all { char ->
                                char.isDigit()
                            }
                        ) {
                            stock = it
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("Stock")
                    },
                    enabled = !isSubmitting
                )

                if (!error.isNullOrBlank()) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    val stockValue =
                        stock.toIntOrNull() ?: 0

                    onSubmit(
                        productId.trim(),
                        name.trim(),
                        category.trim(),
                        stockValue,
                        imageUri
                    )
                },

                enabled =
                    !isSubmitting &&
                            productId.isNotBlank() &&
                            name.isNotBlank() &&
                            category.isNotBlank() &&
                            stock.isNotBlank() &&
                            (
                                    product != null ||
                                            imageUri != null
                                    )
            ) {

                if (isSubmitting) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )

                    Spacer(
                        modifier = Modifier.size(8.dp)
                    )

                    Text(
                        text = if (product == null) {
                            "Adding..."
                        } else {
                            "Updating..."
                        }
                    )

                } else {

                    Text(
                        text = if (product == null) {
                            "Add"
                        } else {
                            "Update"
                        }
                    )
                }
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss,
                enabled = !isSubmitting
            ) {
                Text("Cancel")
            }
        }
    )
}