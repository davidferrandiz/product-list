package com.davidferrandiz.mangostore.core.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.davidferrandiz.mangostore.core.ui.R
import com.davidferrandiz.mangostore.core.ui.theme.MangoTheme
import com.davidferrandiz.mangostore.domain.model.Product

private const val LONG_TITLE =
    "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops, Water Resistant, Unisex"

@PreviewTest
@Preview(name = "ProductCard - not favorite", widthDp = 400)
@Composable
private fun ProductCardNotFavorite() {
    ScreenshotSurface {
        ProductCard(product = sampleProduct(), onToggleFavorite = {})
    }
}

@PreviewTest
@Preview(name = "ProductCard - favorite", widthDp = 400)
@Composable
private fun ProductCardFavorite() {
    ScreenshotSurface {
        ProductCard(product = sampleProduct(isFavorite = true), onToggleFavorite = {})
    }
}

@PreviewTest
@Preview(name = "ProductCard - long title", widthDp = 400)
@Composable
private fun ProductCardLongTitle() {
    ScreenshotSurface {
        ProductCard(product = sampleProduct(title = LONG_TITLE), onToggleFavorite = {})
    }
}

@PreviewTest
@Preview(name = "ProductCard - dark", widthDp = 400, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProductCardDark() {
    ScreenshotSurface {
        ProductCard(product = sampleProduct(isFavorite = true), onToggleFavorite = {})
    }
}

@PreviewTest
@Preview(name = "Empty favorites", widthDp = 400, heightDp = 560)
@Composable
private fun EmptyFavorites() {
    ScreenshotSurface {
        EmptyContent(
            titleRes = R.string.favorites_empty_title,
            messageRes = R.string.favorites_empty,
        )
    }
}

@PreviewTest
@Preview(name = "Error - no connection", widthDp = 400, heightDp = 560)
@Composable
private fun ErrorNoConnection() {
    ScreenshotSurface {
        ErrorContent(messageRes = R.string.error_no_connection, onRetry = {})
    }
}

@PreviewTest
@Preview(
    name = "Error - no connection dark",
    widthDp = 400,
    heightDp = 560,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun ErrorNoConnectionDark() {
    ScreenshotSurface {
        ErrorContent(messageRes = R.string.error_no_connection, onRetry = {})
    }
}

@Composable
private fun ScreenshotSurface(content: @Composable () -> Unit) {
    MangoTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

private fun sampleProduct(
    title: String = "Leather backpack",
    isFavorite: Boolean = false,
) = Product(
    id = 1,
    title = title,
    price = 109.95,
    description = "",
    category = "men's clothing",
    imageUrl = "",
    isFavorite = isFavorite,
)
