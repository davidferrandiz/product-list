package com.davidferrandiz.mangostore.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidferrandiz.mangostore.core.ui.R
import com.davidferrandiz.mangostore.domain.model.Product
import com.davidferrandiz.mangostore.domain.usecase.ObserveFavoritesUseCase
import com.davidferrandiz.mangostore.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

private const val STOP_TIMEOUT_MILLIS = 5_000L

@HiltViewModel
internal class FavoritesViewModel @Inject constructor(
    private val observeFavorites: ObserveFavoritesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {

    private val retryTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<FavoritesUiState> = retryTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            observeFavorites()
                .map { favorites ->
                    if (favorites.isEmpty()) {
                        FavoritesUiState.Empty
                    } else {
                        FavoritesUiState.Content(favorites)
                    }
                }
                .onStart { emit(FavoritesUiState.Loading) }
                .catch { _ ->
                    emit(FavoritesUiState.Error(R.string.error_unknown))
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = FavoritesUiState.Loading,
        )

    private val _messages = Channel<Int>(Channel.BUFFERED)
    val messages: Flow<Int> = _messages.receiveAsFlow()

    fun onRemoveFavorite(product: Product) {
        viewModelScope.launch {
            try {
                toggleFavorite(product)
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _messages.send(R.string.error_favorite_update_failed)
            }
        }
    }

    fun onRetry() {
        viewModelScope.launch { retryTrigger.emit(Unit) }
    }
}
