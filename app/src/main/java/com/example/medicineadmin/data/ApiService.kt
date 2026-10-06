package com.example.medicineadmin.data

import com.example.medicineadmin.model.AvailableStock
import com.example.medicineadmin.model.CategoryListResponse
import com.example.medicineadmin.model.CategoryResponse
import com.example.medicineadmin.model.Notification
import com.example.medicineadmin.model.Order
import com.example.medicineadmin.model.PosterListResponse
import com.example.medicineadmin.model.PosterResponse
import com.example.medicineadmin.model.Product
import com.example.medicineadmin.model.Sale
import com.example.medicineadmin.model.SingleCategoryResponse
import com.example.medicineadmin.model.SinglePosterResponse
import com.example.medicineadmin.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    // ============================================================
    // USER APIs
    // ============================================================

    @GET("getAllUsers")
    suspend fun getAllUsers(): Response<List<User>>

    @FormUrlEncoded
    @POST("getSpacificUser")
    suspend fun getSpecificUser(
        @Field("userId") userId: String
    ): Response<List<User>>

    @FormUrlEncoded
    @PATCH("updateUser")
    suspend fun updateUser(
        @Field("user_id") userId: String,
        @Field("isApproved") isApproved: Int? = null,
        @Field("block") block: Int? = null,
        @Field("password") password: String? = null,
        @Field("name") name: String? = null,
        @Field("address") address: String? = null,
        @Field("email") email: String? = null,
        @Field("phone_number") phoneNumber: String? = null,
        @Field("pincode") pincode: String? = null
    ): Response<ApiResponse>


    // ============================================================
    // PRODUCT APIs
    // ============================================================

    @Multipart
    @POST("addProduct")
    suspend fun addProduct(
        @Part("product_id") productId: RequestBody,
        @Part("product_name") productName: RequestBody,
        @Part("category") category: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part productImage: MultipartBody.Part
    ): Response<ApiResponse>

    @GET("productsDetails")
    suspend fun getProducts(): Response<List<Product>>

    @Multipart
    @PATCH("updateProduct")
    suspend fun updateProduct(
        @Part("product_id") productId: RequestBody,
        @Part("product_name") productName: RequestBody,
        @Part("category") category: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part productImage: MultipartBody.Part? = null
    ): Response<ApiResponse>

    @FormUrlEncoded
    @HTTP(
        method = "DELETE",
        path = "deleteProduct",
        hasBody = true
    )
    suspend fun deleteProduct(
        @Field("product_id") productId: String
    ): Response<ApiResponse>


    // ============================================================
    // ORDER APIs
    // ============================================================

    @FormUrlEncoded
    @POST("addOrder")
    suspend fun addOrder(
        @Field("order_id") orderId: String,
        @Field("user_id") userId: String,
        @Field("product_id") productId: String,
        @Field("isApproved") isApproved: Int,
        @Field("quantity") quantity: Int,
        @Field("date_of_order_creation") dateOfOrderCreation: String,
        @Field("price") price: Double,
        @Field("total_amount") totalAmount: Double,
        @Field("product_name") productName: String,
        @Field("user_name") userName: String,
        @Field("message") message: String,
        @Field("category") category: String
    ): Response<ApiResponse>

    @GET("orders")
    suspend fun getOrders(): Response<List<Order>>

    @GET("userOrders/{user_id}")
    suspend fun getUserOrders(
        @Path("user_id") userId: String
    ): Response<List<Order>>

    @FormUrlEncoded
    @PATCH("updateOrder")
    suspend fun updateOrder(
        @Field("order_id") orderId: String,
        @Field("isApproved") isApproved: Int,
        @Field("quantity") quantity: Int,
        @Field("message") message: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @DELETE("deleteOrder")
    suspend fun deleteOrder(
        @Field("order_id") orderId: String
    ): Response<ApiResponse>


    // ============================================================
    // SELL HISTORY APIs
    // ============================================================

    @FormUrlEncoded
    @POST("addSell")
    suspend fun addSell(
        @Field("sell_id") sellId: String,
        @Field("product_id") productId: String,
        @Field("quantity") quantity: Int,
        @Field("reamining_stock") reaminingStock: Int,
        @Field("date_of_sell") dateOfSell: String,
        @Field("total_amount") totalAmount: Double,
        @Field("price") price: Double,
        @Field("product_name") productName: String,
        @Field("user_name") userName: String,
        @Field("user_id") userId: String
    ): Response<ApiResponse>

    @GET("sellHistory")
    suspend fun getSellHistory(): Response<List<Sale>>

    @GET("userSellHistory/{user_id}")
    suspend fun getUserSellHistory(
        @Path("user_id") userId: String
    ): Response<List<Sale>>

    @FormUrlEncoded
    @DELETE("deleteSell")
    suspend fun deleteSell(
        @Field("sell_id") sellId: String
    ): Response<ApiResponse>


    // ============================================================
    // AVAILABLE STOCK APIs
    // ============================================================

    @FormUrlEncoded
    @POST("addAvailableStock")
    suspend fun addAvailableStock(
        @Field("product_id") productId: String,
        @Field("product_name") productName: String,
        @Field("category") category: String,
        @Field("price") price: Double,
        @Field("stock") stock: Int,
        @Field("user_id") userId: String,
        @Field("user_name") userName: String
    ): Response<ApiResponse>

    @GET("availableStock")
    suspend fun getAvailableStock(): Response<List<AvailableStock>>

    @GET("userAvailableStock/{user_id}")
    suspend fun getUserAvailableStock(
        @Path("user_id") userId: String
    ): Response<List<AvailableStock>>

    @FormUrlEncoded
    @PATCH("updateAvailableStock")
    suspend fun updateAvailableStock(
        @Field("product_id") productId: String,
        @Field("product_name") productName: String,
        @Field("category") category: String,
        @Field("price") price: Double,
        @Field("stock") stock: Int
    ): Response<ApiResponse>

    @FormUrlEncoded
    @DELETE("deleteAvailableStock")
    suspend fun deleteAvailableStock(
        @Field("product_id") productId: String
    ): Response<ApiResponse>


    // ============================================================
    // NOTIFICATION APIs
    // ============================================================

    @GET("notifications")
    suspend fun getNotifications(): Response<List<Notification>>

    @GET("notifications/unread")
    suspend fun getUnreadNotifications(): Response<List<Notification>>

    @GET("notifications/unread-count")
    suspend fun getUnreadNotificationCount(): Response<Map<String, Int>>

    @PATCH("notifications/{notification_id}/read")
    suspend fun markNotificationAsRead(
        @Path("notification_id") notificationId: Int
    ): Response<ApiResponse>

    @PATCH("notifications/read-all")
    suspend fun markAllNotificationsAsRead(): Response<ApiResponse>

    @DELETE("notifications/{notification_id}")
    suspend fun deleteNotification(
        @Path("notification_id") notificationId: Int
    ): Response<ApiResponse>

    @DELETE("notifications")
    suspend fun deleteAllNotifications(): Response<ApiResponse>


    // ============================================================
    // POSTER APIs
    // ============================================================

    @Multipart
    @POST("addPoster")
    suspend fun addPoster(
        @Part("poster_name") posterName: RequestBody,
        @Part posterImage: MultipartBody.Part
    ): Response<PosterResponse>

    @GET("getAllPosters")
    suspend fun getAllPosters(): Response<PosterListResponse>

    @GET("getPoster/{posterId}")
    suspend fun getSpecificPoster(
        @Path("posterId") posterId: Int
    ): Response<SinglePosterResponse>

    @Multipart
    @PUT("updatePoster/{posterId}")
    suspend fun updatePoster(
        @Path("posterId") posterId: Int,
        @Part("poster_name") posterName: RequestBody,
        @Part posterImage: MultipartBody.Part? = null
    ): Response<CommonResponse>

    @DELETE("deletePoster/{posterId}")
    suspend fun deletePoster(
        @Path("posterId") posterId: Int
    ): Response<CommonResponse>


    // ============================================================
    // CATEGORY APIs
    // ============================================================

    @Multipart
    @POST("addCategory")
    suspend fun addCategory(
        @Part("category_name") categoryName: RequestBody,
        @Part categoryImage: MultipartBody.Part
    ): Response<CategoryResponse>

    @GET("getAllCategories")
    suspend fun getAllCategories(): Response<CategoryListResponse>

    @GET("getCategory/{categoryId}")
    suspend fun getSpecificCategory(
        @Path("categoryId") categoryId: Int
    ): Response<SingleCategoryResponse>

    @Multipart
    @PUT("updateCategory/{categoryId}")
    suspend fun updateCategory(
        @Path("categoryId") categoryId: Int,
        @Part("category_name") categoryName: RequestBody,
        @Part categoryImage: MultipartBody.Part? = null
    ): Response<CommonResponse>

    @DELETE("deleteCategory/{categoryId}")
    suspend fun deleteCategory(
        @Path("categoryId") categoryId: Int
    ): Response<CommonResponse>
}