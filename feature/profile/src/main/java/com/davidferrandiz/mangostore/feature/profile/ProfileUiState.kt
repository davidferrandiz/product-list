package com.davidferrandiz.mangostore.feature.profile

import androidx.annotation.StringRes
import com.davidferrandiz.mangostore.domain.model.UserProfile

internal sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Content(val profile: UserProfile, val favoriteCount: Int) : ProfileUiState
    data class Error(@StringRes val messageRes: Int) : ProfileUiState
}
