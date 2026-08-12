package com.davidferrandiz.mangostore.feature.products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidferrandiz.mangostore.core.ui.R
import com.davidferrandiz.mangostore.core.ui.error.toMessageRes
import com.davidferrandiz.mangostore.domain.common.MangoResult
import com.davidferrandiz.mangostore.domain.model.Product
import com.davidferrandiz.mangostore.domain.usecase.GetProductsUseCase
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
internal class ProductsViewModel @Inject constructor(
    private val getProducts: GetProductsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {

    private val retryTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ProductsUiState> = retryTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            getProducts()
                .map { result -> result.toUiState() }
                .onStart { emit(ProductsUiState.Loading) }
                .catch { throwable ->
                    emit(ProductsUiState.Error(R.string.error_unknown))
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = ProductsUiState.Loading,
        )

    private val _messages = Channel<Int>(Channel.BUFFERED)
    val messages: Flow<Int> = _messages.receiveAsFlow()

    fun onToggleFavorite(product: Product) {
        viewModelScope.launch {
            try {
                toggleFavorite(product)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _messages.send(R.string.error_favorite_update_failed)
            }
        }
    }

    fun onRetry() {
        viewModelScope.launch { retryTrigger.emit(Unit) }
    }

    private fun MangoResult<List<Product>>.toUiState(): ProductsUiState = when (this) {
        is MangoResult.Success -> ProductsUiState.Content(data)
        is MangoResult.Error -> ProductsUiState.Error(error.toMessageRes())
    }
}
