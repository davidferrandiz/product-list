package com.davidferrandiz.mangostore.feature.products

import androidx.annotation.StringRes
import com.davidferrandiz.mangostore.domain.model.Product

internal sealed interface ProductsUiState {
    data object Loading : ProductsUiState
    data class Content(val products: List<Product>) : ProductsUiState
    data class Error(@StringRes val messageRes: Int) : ProductsUiState
}
