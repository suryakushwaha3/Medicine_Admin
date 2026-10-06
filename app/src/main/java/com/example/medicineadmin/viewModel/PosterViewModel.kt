package com.example.medicineadmin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicineadmin.data.Repo.PosterRepository
import com.example.medicineadmin.model.Poster
 import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

data class PosterUiState(
    val posters: List<Poster> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val message: String? = null
)

@HiltViewModel
class PosterViewModel @Inject constructor(
    private val repository: PosterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PosterUiState())
    val uiState: StateFlow<PosterUiState> = _uiState.asStateFlow()

    init {
        getAllPosters()
    }

    fun getAllPosters() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            repository.getAllPosters()
                .onSuccess { posters ->
                    _uiState.value = _uiState.value.copy(
                        posters = posters,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load posters."
                    )
                }
        }
    }

    fun addPoster(
        posterName: RequestBody,
        posterImage: MultipartBody.Part
    ) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isSaving = true,
                error = null,
                message = null
            )

            repository.addPoster(
                posterName = posterName,
                posterImage = posterImage
            )
                .onSuccess { response ->

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        message = response.message,
                        error = null
                    )

                    getAllPosters()
                }
                .onFailure { exception ->

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = exception.message ?: "Failed to add poster."
                    )
                }
        }
    }

    fun updatePoster(
        posterId: Int,
        posterName: RequestBody,
        posterImage: MultipartBody.Part? = null
    ) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isSaving = true,
                error = null,
                message = null
            )

            repository.updatePoster(
                posterId = posterId,
                posterName = posterName,
                posterImage = posterImage
            )
                .onSuccess { response ->

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        message = response.message,
                        error = null
                    )

                    getAllPosters()
                }
                .onFailure { exception ->

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = exception.message ?: "Failed to update poster."
                    )
                }
        }
    }

    fun deletePoster(posterId: Int) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isSaving = true,
                error = null,
                message = null
            )

            repository.deletePoster(posterId)
                .onSuccess { response ->

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        message = response.message,
                        error = null
                    )

                    getAllPosters()
                }
                .onFailure { exception ->

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = exception.message ?: "Failed to delete poster."
                    )
                }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(
            message = null
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            error = null
        )
    }
}