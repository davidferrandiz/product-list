package com.davidferrandiz.mangostore.feature.favorites

import app.cash.turbine.test
import com.davidferrandiz.mangostore.core.ui.R
import com.davidferrandiz.mangostore.domain.model.Product
import com.davidferrandiz.mangostore.domain.usecase.ObserveFavoritesUseCase
import com.davidferrandiz.mangostore.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FavoritesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeFavorites: ObserveFavoritesUseCase = mockk()
    private val toggleFavorite: ToggleFavoriteUseCase = mockk(relaxed = true)

    private val favorites = MutableStateFlow(listOf(product(id = 1), product(id = 2)))

    @Test
    fun `emits Empty instead of an empty Content when there is nothing stored`() = runTest {
        favorites.value = emptyList()
        every { observeFavorites() } returns favorites

        viewModel().uiState.test {
            assertEquals(FavoritesUiState.Loading, awaitItem())
            assertEquals(FavoritesUiState.Empty, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Content with the stored favorites`() = runTest {
        every { observeFavorites() } returns favorites

        viewModel().uiState.test {
            assertEquals(FavoritesUiState.Loading, awaitItem())
            assertEquals(FavoritesUiState.Content(favorites.value), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `falls back to Empty when the last favorite is removed`() = runTest {
        every { observeFavorites() } returns favorites

        viewModel().uiState.test {
            assertEquals(FavoritesUiState.Loading, awaitItem())
            assertEquals(FavoritesUiState.Content(favorites.value), awaitItem())

            favorites.value = listOf(product(id = 1))
            assertEquals(FavoritesUiState.Content(listOf(product(id = 1))), awaitItem())

            favorites.value = emptyList()
            assertEquals(FavoritesUiState.Empty, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `an unexpected exception becomes a generic error`() = runTest {
        every { observeFavorites() } returns explodingFlow()

        viewModel().uiState.test {
            assertEquals(FavoritesUiState.Loading, awaitItem())
            assertEquals(FavoritesUiState.Error(R.string.error_unknown), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `delegates the removal to the toggle use case`() = runTest {
        every { observeFavorites() } returns favorites
        val favorite = product(id = 1, isFavorite = true)

        viewModel().onRemoveFavorite(favorite)

        coVerify(exactly = 1) { toggleFavorite(favorite) }
    }

    private fun viewModel() = FavoritesViewModel(observeFavorites, toggleFavorite)
}

private fun explodingFlow(): Flow<List<Product>> = flow {
    throw IllegalStateException("something nobody predicted")
}

private fun product(id: Int, isFavorite: Boolean = true) = Product(
    id = id,
    title = "Product $id",
    price = 9.99,
    description = "Description $id",
    category = "category",
    imageUrl = "https://example.com/$id.png",
    isFavorite = isFavorite,
)
