package com.davidferrandiz.mangostore.feature.products

import app.cash.turbine.test
import com.davidferrandiz.mangostore.core.ui.R
import com.davidferrandiz.mangostore.domain.common.MangoResult
import com.davidferrandiz.mangostore.domain.error.AppError
import com.davidferrandiz.mangostore.domain.model.Product
import com.davidferrandiz.mangostore.domain.usecase.GetProductsUseCase
import com.davidferrandiz.mangostore.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ProductsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getProducts: GetProductsUseCase = mockk()
    private val toggleFavorite: ToggleFavoriteUseCase = mockk(relaxed = true)

    private val catalog = listOf(product(id = 1), product(id = 2))

    @Test
    fun `goes from Loading to Content when the use case succeeds`() = runTest {
        every { getProducts() } returns flowOf(MangoResult.Success(catalog))

        viewModel().uiState.test {
            assertEquals(ProductsUiState.Loading, awaitItem())
            assertEquals(ProductsUiState.Content(catalog), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `translates the domain error into its own message`() = runTest {
        every { getProducts() } returns flowOf(MangoResult.Error(AppError.NoConnection))

        viewModel().uiState.test {
            assertEquals(ProductsUiState.Loading, awaitItem())
            assertEquals(ProductsUiState.Error(R.string.error_no_connection), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry subscribes to the use case again and recovers from the error`() = runTest {
        every { getProducts() } returnsMany listOf(
            flowOf(MangoResult.Error(AppError.Timeout)),
            flowOf(MangoResult.Success(catalog)),
        )

        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(ProductsUiState.Loading, awaitItem())
            assertEquals(ProductsUiState.Error(R.string.error_timeout), awaitItem())

            viewModel.onRetry()

            assertEquals(ProductsUiState.Loading, awaitItem())
            assertEquals(ProductsUiState.Content(catalog), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 2) { getProducts() }
    }

    @Test
    fun `an unexpected exception becomes a generic error without killing the retry`() = runTest {
        every { getProducts() } returnsMany listOf(
            explodingFlow(),
            flowOf(MangoResult.Success(catalog)),
        )

        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(ProductsUiState.Loading, awaitItem())
            assertEquals(ProductsUiState.Error(R.string.error_unknown), awaitItem())

            viewModel.onRetry()

            assertEquals(ProductsUiState.Loading, awaitItem())
            assertEquals(ProductsUiState.Content(catalog), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `delegates the toggle to the use case`() = runTest {
        every { getProducts() } returns flowOf(MangoResult.Success(catalog))
        val favorite = product(id = 1)

        viewModel().onToggleFavorite(favorite)

        coVerify(exactly = 1) { toggleFavorite(favorite) }
    }

    private fun viewModel() = ProductsViewModel(getProducts, toggleFavorite)
}

private fun explodingFlow(): Flow<MangoResult<List<Product>>> = flow {
    throw IllegalStateException("something nobody predicted")
}

private fun product(id: Int) = Product(
    id = id,
    title = "Product $id",
    price = 9.99,
    description = "Description $id",
    category = "category",
    imageUrl = "https://example.com/$id.png",
)
