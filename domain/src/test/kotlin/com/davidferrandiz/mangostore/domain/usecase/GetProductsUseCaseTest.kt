package com.davidferrandiz.mangostore.domain.usecase

import app.cash.turbine.test
import com.davidferrandiz.mangostore.domain.common.MangoResult
import com.davidferrandiz.mangostore.domain.error.AppError
import com.davidferrandiz.mangostore.domain.fake.FakeFavoriteRepository
import com.davidferrandiz.mangostore.domain.fake.product
import com.davidferrandiz.mangostore.domain.model.Product
import com.davidferrandiz.mangostore.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetProductsUseCaseTest {

    private val productRepository: ProductRepository = mockk()
    private val favoriteRepository = FakeFavoriteRepository()

    private val getProducts = GetProductsUseCase(productRepository, favoriteRepository)

    private val catalog = listOf(product(id = 1), product(id = 2), product(id = 3))

    @Test
    fun `marks as favorite only the products already stored as favorites`() = runTest {
        coEvery { productRepository.getProducts() } returns MangoResult.Success(catalog)
        favoriteRepository.setFavorites(listOf(product(id = 2)))

        getProducts().test {
            val products = awaitItem().successData()

            assertEquals(listOf(1, 2, 3), products.map { product -> product.id })
            assertEquals(setOf(2), products.favoriteIds())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `re-emits with the new favorites without calling the network again`() = runTest {
        coEvery { productRepository.getProducts() } returns MangoResult.Success(catalog)
        favoriteRepository.setFavorites(listOf(product(id = 2)))

        getProducts().test {
            assertEquals(setOf(2), awaitItem().successData().favoriteIds())

            favoriteRepository.setFavorites(listOf(product(id = 2), product(id = 3)))

            assertEquals(setOf(2, 3), awaitItem().successData().favoriteIds())

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { productRepository.getProducts() }
    }

    @Test
    fun `emits the error and never subscribes to favorites when the network fails`() = runTest {
        coEvery { productRepository.getProducts() } returns MangoResult.Error(AppError.NoConnection)

        getProducts().test {
            val result = awaitItem()

            assertTrue(result is MangoResult.Error)
            assertEquals(AppError.NoConnection, (result as MangoResult.Error).error)
            awaitComplete()
        }

        assertEquals(0, favoriteRepository.observeFavoriteIdsSubscriptions)
    }
}

private fun MangoResult<List<Product>>.successData(): List<Product> {
    assertTrue("Expected Success but was $this", this is MangoResult.Success)
    return (this as MangoResult.Success).data
}

private fun List<Product>.favoriteIds(): Set<Int> =
    filter { product -> product.isFavorite }
        .map { product -> product.id }
        .toSet()
