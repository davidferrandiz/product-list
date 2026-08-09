package com.davidferrandiz.mangostore.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidferrandiz.mangostore.core.ui.R
import com.davidferrandiz.mangostore.domain.model.Product
import com.davidferrandiz.mangostore.domain.usecase.ObserveFavoritesUseCase
import com.davidferrandiz.mangostore.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val STOP_TIMEOUT_MILLIS = 5_000L

@HiltViewModel
internal class FavoritesViewModel @Inject constructor(
    observeFavorites: ObserveFavoritesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = observeFavorites()
        .map { favorites ->
            if (favorites.isEmpty()) {
                FavoritesUiState.Empty
            } else {
                FavoritesUiState.Content(favorites)
            }
        }
        .catch { _ ->
            emit(FavoritesUiState.Error(R.string.error_unknown))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = FavoritesUiState.Loading,
        )

    fun onRemoveFavorite(product: Product) {
        viewModelScope.launch { toggleFavorite(product) }
    }
}
