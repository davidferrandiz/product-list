package com.davidferrandiz.mangostore.feature.favorites

import androidx.annotation.StringRes
import com.davidferrandiz.mangostore.domain.model.Product

internal sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data object Empty : FavoritesUiState
    data class Content(val products: List<Product>) : FavoritesUiState
    data class Error(@StringRes val messageRes: Int) : FavoritesUiState
}
