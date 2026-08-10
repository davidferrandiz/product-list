package com.davidferrandiz.mangostore.feature.products

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.davidferrandiz.mangostore.core.ui.R
import com.davidferrandiz.mangostore.core.ui.component.LOADING_INDICATOR_TAG
import com.davidferrandiz.mangostore.core.ui.theme.MangoTheme
import com.davidferrandiz.mangostore.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductsContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val catalog = listOf(
        product(id = 1, title = "Leather backpack", isFavorite = true),
        product(id = 2, title = "Cotton t-shirt", isFavorite = false),
    )

    @Test
    fun showsTheLoadingIndicatorWhileLoading() {
        setContent(ProductsUiState.Loading)

        composeRule.onNodeWithContentDescription(LOADING_INDICATOR_TAG).assertIsDisplayed()
    }

    @Test
    fun showsTheErrorMessageAndLetsTheUserRetry() {
        var retried = false
        setContent(
            uiState = ProductsUiState.Error(R.string.error_no_connection),
            onRetry = { retried = true },
        )

        composeRule.onNodeWithText(context.getString(R.string.error_no_connection))
            .assertIsDisplayed()

        composeRule.onNodeWithText(context.getString(R.string.action_retry)).performClick()

        assertTrue(retried)
    }

    @Test
    fun showsEveryProductInTheCatalogue() {
        setContent(ProductsUiState.Content(catalog))

        composeRule.onNodeWithText("Leather backpack").assertIsDisplayed()
        composeRule.onNodeWithText("Cotton t-shirt").assertIsDisplayed()
    }

    @Test
    fun showsAFilledHeartForFavoritesAndAnEmptyOneForTheRest() {
        setContent(ProductsUiState.Content(catalog))

        composeRule.onNodeWithContentDescription(removeLabel).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(addLabel).assertIsDisplayed()
    }

    @Test
    fun reportsTheProductWhoseHeartWasTapped() {
        val toggled = mutableListOf<Product>()
        setContent(
            uiState = ProductsUiState.Content(catalog),
            onToggleFavorite = { product -> toggled += product },
        )

        composeRule.onNodeWithContentDescription(addLabel).performClick()
        composeRule.onNodeWithContentDescription(removeLabel).performClick()

        assertEquals(listOf(2, 1), toggled.map { product -> product.id })
    }

    private val addLabel: String
        get() = context.getString(R.string.product_add_to_favorites)

    private val removeLabel: String
        get() = context.getString(R.string.product_remove_from_favorites)

    private fun setContent(
        uiState: ProductsUiState,
        onRetry: () -> Unit = {},
        onToggleFavorite: (Product) -> Unit = {},
    ) {
        composeRule.setContent {
            MangoTheme {
                ProductsContent(
                    uiState = uiState,
                    onRetry = onRetry,
                    onToggleFavorite = onToggleFavorite,
                )
            }
        }
    }
}

private fun product(id: Int, title: String, isFavorite: Boolean) = Product(
    id = id,
    title = title,
    price = 9.99,
    description = "Description $id",
    category = "category",
    imageUrl = "",
    isFavorite = isFavorite,
)
