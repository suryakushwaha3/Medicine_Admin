package com.example.medicineadmin.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicineadmin.data.Repo.Repository
import com.example.medicineadmin.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UsersUiState(
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val users: List<User> = emptyList(),
    val selectedUser: User? = null,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        UsersUiState()
    )

    val uiState: StateFlow<UsersUiState> =
        _uiState.asStateFlow()


    // ============================================================
    // GET ALL USERS
    // ============================================================

    init {
        getAllUsers()
    }

    fun getAllUsers() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {

                val response =
                    repository.getAllUsers()

                Log.d(
                    "BLOCK_DEBUG",
                    "GET_ALL_USERS HTTP_CODE=${response.code()}"
                )

                Log.d(
                    "BLOCK_DEBUG",
                    "GET_ALL_USERS SUCCESS=${response.isSuccessful}"
                )

                if (response.isSuccessful) {

                    val users =
                        response.body().orEmpty()

                    // ====================================================
                    // DEBUG - BACKEND SE AAYA DATA
                    // ====================================================

                    users.forEach { user ->

                        Log.d(
                            "BLOCK_DEBUG",
                            "USER -> " +
                                    "user_id=${user.user_id}, " +
                                    "name=${user.name}, " +
                                    "isApproved=${user.isApproved}, " +
                                    "block=${user.block}"
                        )
                    }

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            users = users,
                            error = null
                        )

                } else {

                    val errorBody =
                        response.errorBody()?.string()

                    Log.e(
                        "BLOCK_DEBUG",
                        "GET_ALL_USERS ERROR=$errorBody"
                    )

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            error =
                                "Failed to load users. Code: ${response.code()}"
                        )
                }

            } catch (e: Exception) {

                Log.e(
                    "BLOCK_DEBUG",
                    "GET_ALL_USERS EXCEPTION",
                    e
                )

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        error =
                            e.message
                                ?: "Something went wrong while loading users."
                    )
            }
        }
    }


    // ============================================================
    // REFRESH USERS
    // ============================================================

    fun refreshUsers() {
        getAllUsers()
    }


    // ============================================================
    // GET SPECIFIC USER
    // ============================================================

    fun getSpecificUser(
        userId: String
    ) {

        if (userId.isBlank()) {

            _uiState.value =
                _uiState.value.copy(
                    error = "User ID cannot be empty."
                )

            return
        }

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

            try {

                val response =
                    repository.getSpecificUser(userId)

                if (response.isSuccessful) {

                    val users =
                        response.body().orEmpty()

                    val user =
                        users.firstOrNull()

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            selectedUser = user,
                            error =
                                if (user == null) {
                                    "User not found."
                                } else {
                                    null
                                }
                        )

                } else {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            error =
                                "Failed to get user. Code: ${response.code()}"
                        )
                }

            } catch (e: Exception) {

                Log.e(
                    "USER_DEBUG",
                    "GET_SPECIFIC_USER EXCEPTION",
                    e
                )

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        error =
                            e.message
                                ?: "Something went wrong while getting user."
                    )
            }
        }
    }


    // ============================================================
    // UPDATE USER
    // ============================================================

    fun updateUser(
        userId: String,

        // 1 = Approved
        // 0 = Pending
        isApproved: Int? = null,

        // 1 = Blocked
        // 0 = Unblocked
        block: Int? = null,

        password: String? = null,
        name: String? = null,
        address: String? = null,
        email: String? = null,
        phoneNumber: String? = null,
        pincode: String? = null
    ) {

        if (userId.isBlank()) {

            _uiState.value =
                _uiState.value.copy(
                    error = "User ID cannot be empty."
                )

            return
        }

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isUpdating = true,
                    error = null,
                    successMessage = null
                )

            try {

                // ====================================================
                // REQUEST DEBUG
                // ====================================================

                Log.d(
                    "BLOCK_DEBUG",
                    "========================================"
                )

                Log.d(
                    "BLOCK_DEBUG",
                    "UPDATE USER REQUEST"
                )

                Log.d(
                    "BLOCK_DEBUG",
                    "userId=$userId"
                )

                Log.d(
                    "BLOCK_DEBUG",
                    "isApproved=$isApproved"
                )

                Log.d(
                    "BLOCK_DEBUG",
                    "block=$block"
                )

                Log.d(
                    "BLOCK_DEBUG",
                    "========================================"
                )


                // ====================================================
                // API CALL
                // ====================================================

                val response =
                    repository.updateUser(

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


                // ====================================================
                // RESPONSE DEBUG
                // ====================================================

                Log.d(
                    "BLOCK_DEBUG",
                    "HTTP_CODE=${response.code()}"
                )

                Log.d(
                    "BLOCK_DEBUG",
                    "SUCCESS=${response.isSuccessful}"
                )

                Log.d(
                    "BLOCK_DEBUG",
                    "BODY=${response.body()}"
                )

                Log.d(
                    "BLOCK_DEBUG",
                    "ERROR_BODY=${response.errorBody()?.string()}"
                )


                // ====================================================
                // SUCCESS
                // ====================================================

                if (response.isSuccessful) {

                    val message =
                        response.body()?.message
                            ?: when {

                                block == 1 ->
                                    "User blocked successfully."

                                block == 0 ->
                                    "User unblocked successfully."

                                isApproved == 1 ->
                                    "User approved successfully."

                                else ->
                                    "User updated successfully."
                            }


                    _uiState.value =
                        _uiState.value.copy(
                            isUpdating = false,
                            successMessage = message,
                            error = null
                        )


                    // ====================================================
                    // IMPORTANT
                    //
                    // Backend se fresh data reload hoga.
                    //
                    // Block:
                    // block = 1
                    //
                    // Unblock:
                    // block = 0
                    //
                    // Iske baad UsersScreen automatically
                    // status ke according tab change karega.
                    // ====================================================

                    getAllUsers()

                } else {

                    val errorMessage =
                        response.body()?.error
                            ?: "Failed to update user. Code: ${response.code()}"

                    _uiState.value =
                        _uiState.value.copy(
                            isUpdating = false,
                            error = errorMessage,
                            successMessage = null
                        )
                }

            } catch (e: Exception) {

                Log.e(
                    "BLOCK_DEBUG",
                    "UPDATE USER EXCEPTION",
                    e
                )

                _uiState.value =
                    _uiState.value.copy(
                        isUpdating = false,
                        error =
                            e.message
                                ?: "Something went wrong while updating user.",
                        successMessage = null
                    )
            }
        }
    }


    // ============================================================
    // BLOCK USER
    // ============================================================

    fun blockUser(
        userId: String
    ) {

        Log.d(
            "BLOCK_DEBUG",
            "BLOCK BUTTON CLICKED"
        )

        Log.d(
            "BLOCK_DEBUG",
            "BLOCK USER ID=$userId"
        )

        updateUser(
            userId = userId,

            // Block user
            block = 1
        )
    }


    // ============================================================
    // UNBLOCK USER
    // ============================================================

    fun unblockUser(
        userId: String
    ) {

        Log.d(
            "BLOCK_DEBUG",
            "UNBLOCK BUTTON CLICKED"
        )

        Log.d(
            "BLOCK_DEBUG",
            "UNBLOCK USER ID=$userId"
        )

        updateUser(
            userId = userId,

            // Unblock user
            block = 0
        )
    }


    // ============================================================
    // APPROVE USER
    // ============================================================

    fun approveUser(
        userId: String
    ) {

        Log.d(
            "BLOCK_DEBUG",
            "APPROVE BUTTON CLICKED"
        )

        Log.d(
            "BLOCK_DEBUG",
            "APPROVE USER ID=$userId"
        )

        updateUser(
            userId = userId,

            // Approve user
            isApproved = 1
        )
    }


    // ============================================================
    // CLEAR ERROR
    // ============================================================

    fun clearError() {

        _uiState.value =
            _uiState.value.copy(
                error = null
            )
    }


    // ============================================================
    // CLEAR SUCCESS MESSAGE
    // ============================================================

    fun clearSuccessMessage() {

        _uiState.value =
            _uiState.value.copy(
                successMessage = null
            )
    }


    // ============================================================
    // CLEAR SELECTED USER
    // ============================================================

    fun clearSelectedUser() {

        _uiState.value =
            _uiState.value.copy(
                selectedUser = null
            )
    }
}