package com.example.medicineadmin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicineadmin.data.Repo.Repository
import com.example.medicineadmin.model.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


// ============================================================
// NOTIFICATION UI STATE
// ============================================================

data class NotificationUiState(
    val notifications: List<Notification> = emptyList(),
    val unreadNotifications: List<Notification> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)


// ============================================================
// NOTIFICATION VIEW MODEL
// ============================================================

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        NotificationUiState()
    )

    val uiState: StateFlow<NotificationUiState> =
        _uiState.asStateFlow()


    // ========================================================
    // GET ALL NOTIFICATIONS
    // ========================================================

    fun getNotifications() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {

                val response = repository.getNotifications()

                if (response.isSuccessful) {

                    _uiState.value = _uiState.value.copy(
                        notifications = response.body().orEmpty(),
                        isLoading = false,
                        error = null
                    )

                } else {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error =
                            "Failed to fetch notifications. Code: ${response.code()}"
                    )
                }

            } catch (error: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error =
                        error.message ?: "Something went wrong."
                )
            }
        }
    }


    // ========================================================
    // GET UNREAD NOTIFICATIONS
    // ========================================================

    fun getUnreadNotifications() {

        viewModelScope.launch {

            try {

                val response =
                    repository.getUnreadNotifications()

                if (response.isSuccessful) {

                    _uiState.value = _uiState.value.copy(
                        unreadNotifications =
                            response.body().orEmpty(),
                        error = null
                    )

                } else {

                    _uiState.value = _uiState.value.copy(
                        error =
                            "Failed to fetch unread notifications. Code: ${response.code()}"
                    )
                }

            } catch (error: Exception) {

                _uiState.value = _uiState.value.copy(
                    error =
                        error.message ?: "Something went wrong."
                )
            }
        }
    }


    // ========================================================
    // GET UNREAD COUNT
    // ========================================================

    fun getUnreadNotificationCount() {

        viewModelScope.launch {

            try {

                val response =
                    repository.getUnreadNotificationCount()

                if (response.isSuccessful) {

                    val count =
                        response.body()?.get("count") ?: 0

                    _uiState.value = _uiState.value.copy(
                        unreadCount = count,
                        error = null
                    )

                } else {

                    _uiState.value = _uiState.value.copy(
                        error =
                            "Failed to fetch notification count. Code: ${response.code()}"
                    )
                }

            } catch (error: Exception) {

                _uiState.value = _uiState.value.copy(
                    error =
                        error.message ?: "Something went wrong."
                )
            }
        }
    }


    // ========================================================
    // MARK SINGLE NOTIFICATION AS READ
    // ========================================================

    fun markNotificationAsRead(
        notificationId: Int
    ) {

        viewModelScope.launch {

            try {

                val response =
                    repository.markNotificationAsRead(
                        notificationId = notificationId
                    )

                if (response.isSuccessful) {

                    refreshNotifications()

                } else {

                    _uiState.value = _uiState.value.copy(
                        error =
                            "Failed to mark notification as read. Code: ${response.code()}"
                    )
                }

            } catch (error: Exception) {

                _uiState.value = _uiState.value.copy(
                    error =
                        error.message ?: "Something went wrong."
                )
            }
        }
    }


    // ========================================================
    // MARK ALL NOTIFICATIONS AS READ
    // ========================================================

    fun markAllNotificationsAsRead() {

        viewModelScope.launch {

            try {

                val response =
                    repository.markAllNotificationsAsRead()

                if (response.isSuccessful) {

                    refreshNotifications()

                } else {

                    _uiState.value = _uiState.value.copy(
                        error =
                            "Failed to mark all notifications as read. Code: ${response.code()}"
                    )
                }

            } catch (error: Exception) {

                _uiState.value = _uiState.value.copy(
                    error =
                        error.message ?: "Something went wrong."
                )
            }
        }
    }


    // ========================================================
    // DELETE SINGLE NOTIFICATION
    // ========================================================

    fun deleteNotification(
        notificationId: Int
    ) {

        viewModelScope.launch {

            try {

                val response =
                    repository.deleteNotification(
                        notificationId = notificationId
                    )

                if (response.isSuccessful) {

                    refreshNotifications()

                } else {

                    _uiState.value = _uiState.value.copy(
                        error =
                            "Failed to delete notification. Code: ${response.code()}"
                    )
                }

            } catch (error: Exception) {

                _uiState.value = _uiState.value.copy(
                    error =
                        error.message ?: "Something went wrong."
                )
            }
        }
    }


    // ========================================================
    // DELETE ALL NOTIFICATIONS
    // ========================================================

    fun deleteAllNotifications() {

        viewModelScope.launch {

            try {

                val response =
                    repository.deleteAllNotifications()

                if (response.isSuccessful) {

                    refreshNotifications()

                } else {

                    _uiState.value = _uiState.value.copy(
                        error =
                            "Failed to delete all notifications. Code: ${response.code()}"
                    )
                }

            } catch (error: Exception) {

                _uiState.value = _uiState.value.copy(
                    error =
                        error.message ?: "Something went wrong."
                )
            }
        }
    }


    // ========================================================
    // REFRESH NOTIFICATIONS
    // ========================================================

    fun refreshNotifications() {

        getNotifications()
        getUnreadNotifications()
        getUnreadNotificationCount()
    }


    // ========================================================
    // CLEAR ERROR
    // ========================================================

    fun clearError() {

        _uiState.value = _uiState.value.copy(
            error = null
        )
    }
}