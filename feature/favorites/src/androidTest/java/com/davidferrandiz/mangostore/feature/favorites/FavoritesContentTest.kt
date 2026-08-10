package com.davidferrandiz.mangostore.feature.favorites

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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val favorites = listOf(
        product(id = 1, title = "Leather backpack"),
        product(id = 2, title = "Cotton t-shirt"),
    )

    @Test
    fun showsTheLoadingIndicatorWhileLoading() {
        setContent(FavoritesUiState.Loading)

        composeRule.onNodeWithContentDescription(LOADING_INDICATOR_TAG).assertIsDisplayed()
    }

    @Test
    fun showsTheEmptyStateInsteadOfABlankScreen() {
        setContent(FavoritesUiState.Empty)

        composeRule.onNodeWithText(context.getString(R.string.favorites_empty_title))
            .assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.favorites_empty))
            .assertIsDisplayed()
    }

    @Test
    fun showsEveryStoredFavorite() {
        setContent(FavoritesUiState.Content(favorites))

        composeRule.onNodeWithText("Leather backpack").assertIsDisplayed()
        composeRule.onNodeWithText("Cotton t-shirt").assertIsDisplayed()
    }

    @Test
    fun reportsTheFavoriteThatWasRemoved() {
        val removed = mutableListOf<Product>()
        setContent(
            uiState = FavoritesUiState.Content(listOf(product(id = 7, title = "Silver ring"))),
            onRemoveFavorite = { product -> removed += product },
        )

        composeRule
            .onNodeWithContentDescription(context.getString(R.string.product_remove_from_favorites))
            .performClick()

        assertEquals(listOf(7), removed.map { product -> product.id })
    }

    private fun setContent(
        uiState: FavoritesUiState,
        onRemoveFavorite: (Product) -> Unit = {},
    ) {
        composeRule.setContent {
            MangoTheme {
                FavoritesContent(
                    uiState = uiState,
                    onRemoveFavorite = onRemoveFavorite,
                )
            }
        }
    }
}

private fun product(id: Int, title: String) = Product(
    id = id,
    title = title,
    price = 9.99,
    description = "Description $id",
    category = "category",
    imageUrl = "https://example.com/$id.png",
    isFavorite = true,
)
