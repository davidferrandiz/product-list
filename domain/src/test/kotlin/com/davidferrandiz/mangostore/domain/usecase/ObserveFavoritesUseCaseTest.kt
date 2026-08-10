package com.davidferrandiz.mangostore.domain.usecase

import app.cash.turbine.test
import com.davidferrandiz.mangostore.core.testing.FakeFavoriteRepository
import com.davidferrandiz.mangostore.core.testing.product
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ObserveFavoritesUseCaseTest {

    private val favoriteRepository = FakeFavoriteRepository()

    private val observeFavorites = ObserveFavoritesUseCase(favoriteRepository)

    @Test
    fun `guarantees isFavorite is true whatever the data layer returns`() = runTest {
        favoriteRepository.setFavorites(
            listOf(product(id = 1, isFavorite = false), product(id = 2, isFavorite = false))
        )

        observeFavorites().test {
            val favorites = awaitItem()

            assertEquals(listOf(1, 2), favorites.map { favorite -> favorite.id })
            assertTrue(favorites.all { favorite -> favorite.isFavorite })

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `re-emits every time the stored favorites change`() = runTest {
        favoriteRepository.setFavorites(listOf(product(id = 1)))

        observeFavorites().test {
            assertEquals(listOf(1), awaitItem().map { favorite -> favorite.id })

            favoriteRepository.setFavorites(emptyList())

            assertEquals(emptyList<Int>(), awaitItem().map { favorite -> favorite.id })

            cancelAndIgnoreRemainingEvents()
        }
    }
}
