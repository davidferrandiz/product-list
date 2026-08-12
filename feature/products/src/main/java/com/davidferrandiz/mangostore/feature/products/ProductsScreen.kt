package com.davidferrandiz.mangostore.feature.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidferrandiz.mangostore.core.ui.component.ErrorContent
import com.davidferrandiz.mangostore.core.ui.component.LoadingIndicator
import com.davidferrandiz.mangostore.core.ui.component.ProductCard
import com.davidferrandiz.mangostore.domain.model.Product

@Composable
fun ProductsScreen(modifier: Modifier = Modifier) {
    val viewModel: ProductsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val resources = LocalResources.current

    LaunchedEffect(viewModel) {
        viewModel.messages.collect { messageRes ->
            snackbarHostState.showSnackbar(resources.getString(messageRes))
        }
    }

    Box(modifier = modifier) {
        ProductsContent(
            uiState = uiState,
            onRetry = viewModel::onRetry,
            onToggleFavorite = viewModel::onToggleFavorite,
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
internal fun ProductsContent(
    uiState: ProductsUiState,
    onRetry: () -> Unit,
    onToggleFavorite: (Product) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        ProductsUiState.Loading -> LoadingIndicator(modifier)

        is ProductsUiState.Error -> ErrorContent(
            messageRes = uiState.messageRes,
            onRetry = onRetry,
            modifier = modifier,
        )

        is ProductsUiState.Content -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = uiState.products,
                key = { product -> product.id },
            ) { product ->
                ProductCard(
                    product = product,
                    onToggleFavorite = onToggleFavorite,
                )
            }
        }
    }
}
