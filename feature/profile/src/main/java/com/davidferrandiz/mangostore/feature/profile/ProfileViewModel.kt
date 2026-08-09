package com.davidferrandiz.mangostore.feature.profile

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidferrandiz.mangostore.core.ui.R
import com.davidferrandiz.mangostore.core.ui.error.toMessageRes
import com.davidferrandiz.mangostore.domain.common.MangoResult
import com.davidferrandiz.mangostore.domain.model.UserProfile
import com.davidferrandiz.mangostore.domain.usecase.GetUserProfileUseCase
import com.davidferrandiz.mangostore.domain.usecase.ObserveFavoriteCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val STOP_TIMEOUT_MILLIS = 5_000L

private sealed interface ProfileLoad {
    data object Loading : ProfileLoad
    data class Success(val profile: UserProfile) : ProfileLoad
    data class Error(@StringRes val messageRes: Int) : ProfileLoad
}

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    private val getUserProfile: GetUserProfileUseCase,
    observeFavoriteCount: ObserveFavoriteCountUseCase,
) : ViewModel() {

    private val profileLoad = MutableStateFlow<ProfileLoad>(ProfileLoad.Loading)

    val uiState: StateFlow<ProfileUiState> = combine(
        profileLoad,
        observeFavoriteCount(),
    ) { load, favoriteCount ->
        when (load) {
            ProfileLoad.Loading -> ProfileUiState.Loading
            is ProfileLoad.Success -> ProfileUiState.Content(load.profile, favoriteCount)
            is ProfileLoad.Error -> ProfileUiState.Error(load.messageRes)
        }
    }
        .onStart { loadProfile() }
        .catch { _ ->
            emit(ProfileUiState.Error(R.string.error_unknown))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = ProfileUiState.Loading,
        )

    fun onRetry() {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            profileLoad.value = ProfileLoad.Loading
            profileLoad.value = when (val result = getUserProfile()) {
                is MangoResult.Success -> ProfileLoad.Success(result.data)
                is MangoResult.Error -> ProfileLoad.Error(result.error.toMessageRes())
            }
        }
    }
}
