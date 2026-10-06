package com.example.medicineadmin.data.Repo

import android.content.Context
import android.net.Uri
import com.example.medicineadmin.data.ApiResponse
import com.example.medicineadmin.data.ApiService
import com.example.medicineadmin.model.AvailableStock
import com.example.medicineadmin.model.Notification
import com.example.medicineadmin.model.Order
import com.example.medicineadmin.model.Product
import com.example.medicineadmin.model.Sale
import com.example.medicineadmin.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) {

    // User APIs

    suspend fun getAllUsers(): Response<List<User>> =
        apiService.getAllUsers()

    suspend fun getSpecificUser(
        userId: String
    ): Response<List<User>> =
        apiService.getSpecificUser(userId)

    suspend fun updateUser(
        userId: String,
        isApproved: Int? = null,
        block: Int? = null,
        password: String? = null,
        name: String? = null,
        address: String? = null,
        email: String? = null,
        phoneNumber: String? = null,
        pincode: String? = null
    ): Response<ApiResponse> =
        apiService.updateUser(
            userId = userId,
            isApproved = isApproved,
            block = block,
            password = password,
            name = name,
            address = address,
            email = email,
            phoneNumber = phoneNumber,
            pincode = pincode
        )

    // Product APIs

    suspend fun addProduct(
        productId: String,
        productName: String,
        category: String,
        stock: Int,
        imageUri: Uri
    ): Response<ApiResponse> {

        val productIdBody = productId.toTextRequestBody()
        val productNameBody = productName.toTextRequestBody()
        val categoryBody = category.toTextRequestBody()
        val stockBody = stock.toString().toTextRequestBody()

        val imagePart = createImagePart(imageUri)

        return apiService.addProduct(
            productId = productIdBody,
            productName = productNameBody,
            category = categoryBody,
            stock = stockBody,
            productImage = imagePart
        )
    }

    suspend fun getProducts(): Response<List<Product>> =
        apiService.getProducts()

    suspend fun updateProduct(
        productId: String,
        productName: String,
        category: String,
        stock: Int,
        imageUri: Uri? = null
    ): Response<ApiResponse> {

        val productIdBody = productId.toTextRequestBody()
        val productNameBody = productName.toTextRequestBody()
        val categoryBody = category.toTextRequestBody()
        val stockBody = stock.toString().toTextRequestBody()

        val imagePart = imageUri?.let {
            createImagePart(it)
        }

        return apiService.updateProduct(
            productId = productIdBody,
            productName = productNameBody,
            category = categoryBody,
            stock = stockBody,
            productImage = imagePart
        )
    }

    suspend fun deleteProduct(
        productId: String
    ): Response<ApiResponse> =
        apiService.deleteProduct(productId)

    // Order APIs

    suspend fun addOrder(
        orderId: String,
        userId: String,
        productId: String,
        isApproved: Int,
        quantity: Int,
        dateOfOrderCreation: String,
        price: Double,
        totalAmount: Double,
        productName: String,
        userName: String,
        message: String,
        category: String
    ): Response<ApiResponse> =
        apiService.addOrder(
            orderId = orderId,
            userId = userId,
            productId = productId,
            isApproved = isApproved,
            quantity = quantity,
            dateOfOrderCreation = dateOfOrderCreation,
            price = price,
            totalAmount = totalAmount,
            productName = productName,
            userName = userName,
            message = message,
            category = category
        )

    suspend fun getOrders(): Response<List<Order>> =
        apiService.getOrders()

    suspend fun getUserOrders(
        userId: String
    ): Response<List<Order>> =
        apiService.getUserOrders(userId)

    suspend fun updateOrder(
        orderId: String,
        isApproved: Int,
        quantity: Int,
        message: String
    ): Response<ApiResponse> =
        apiService.updateOrder(
            orderId = orderId,
            isApproved = isApproved,
            quantity = quantity,
            message = message
        )

    suspend fun deleteOrder(
        orderId: String
    ): Response<ApiResponse> =
        apiService.deleteOrder(orderId)

    // Sell History APIs

    suspend fun addSell(
        sellId: String,
        productId: String,
        quantity: Int,
        reaminingStock: Int,
        dateOfSell: String,
        totalAmount: Double,
        price: Double,
        productName: String,
        userName: String,
        userId: String
    ): Response<ApiResponse> =
        apiService.addSell(
            sellId = sellId,
            productId = productId,
            quantity = quantity,
            reaminingStock = reaminingStock,
            dateOfSell = dateOfSell,
            totalAmount = totalAmount,
            price = price,
            productName = productName,
            userName = userName,
            userId = userId
        )

    suspend fun getSellHistory(): Response<List<Sale>> =
        apiService.getSellHistory()

    suspend fun getUserSellHistory(
        userId: String
    ): Response<List<Sale>> =
        apiService.getUserSellHistory(userId)

    suspend fun deleteSell(
        sellId: String
    ): Response<ApiResponse> =
        apiService.deleteSell(sellId)

    // Available Stock APIs

    suspend fun addAvailableStock(
        productId: String,
        productName: String,
        category: String,
        price: Double,
        stock: Int,
        userId: String,
        userName: String
    ): Response<ApiResponse> =
        apiService.addAvailableStock(
            productId = productId,
            productName = productName,
            category = category,
            price = price,
            stock = stock,
            userId = userId,
            userName = userName
        )

    suspend fun getAvailableStock(): Response<List<AvailableStock>> =
        apiService.getAvailableStock()

    suspend fun getUserAvailableStock(
        userId: String
    ): Response<List<AvailableStock>> =
        apiService.getUserAvailableStock(userId)

    suspend fun updateAvailableStock(
        productId: String,
        productName: String,
        category: String,
        price: Double,
        stock: Int
    ): Response<ApiResponse> =
        apiService.updateAvailableStock(
            productId = productId,
            productName = productName,
            category = category,
            price = price,
            stock = stock
        )

    suspend fun deleteAvailableStock(
        productId: String
    ): Response<ApiResponse> =
        apiService.deleteAvailableStock(productId)

    // Notification APIs

    suspend fun getNotifications(): Response<List<Notification>> =
        apiService.getNotifications()

    suspend fun getUnreadNotifications(): Response<List<Notification>> =
        apiService.getUnreadNotifications()

    suspend fun getUnreadNotificationCount(): Response<Map<String, Int>> =
        apiService.getUnreadNotificationCount()

    suspend fun markNotificationAsRead(
        notificationId: Int
    ): Response<ApiResponse> =
        apiService.markNotificationAsRead(notificationId)

    suspend fun markAllNotificationsAsRead(): Response<ApiResponse> =
        apiService.markAllNotificationsAsRead()

    suspend fun deleteNotification(
        notificationId: Int
    ): Response<ApiResponse> =
        apiService.deleteNotification(notificationId)

    suspend fun deleteAllNotifications(): Response<ApiResponse> =
        apiService.deleteAllNotifications()

    private fun String.toTextRequestBody(): RequestBody =
        toRequestBody("text/plain".toMediaTypeOrNull())

    private fun createImagePart(
        uri: Uri
    ): MultipartBody.Part {

        val inputStream =
            context.contentResolver.openInputStream(uri)
                ?: throw IllegalArgumentException(
                    "Unable to read selected image"
                )

        val imageBytes = inputStream.use {
            it.readBytes()
        }

        val mimeType =
            context.contentResolver.getType(uri)
                ?: "image/jpeg"

        val imageRequestBody =
            imageBytes.toRequestBody(
                mimeType.toMediaTypeOrNull()
            )

        return MultipartBody.Part.createFormData(
            name = "product_image",
            filename = "product_image.jpg",
            body = imageRequestBody
        )
    }
}