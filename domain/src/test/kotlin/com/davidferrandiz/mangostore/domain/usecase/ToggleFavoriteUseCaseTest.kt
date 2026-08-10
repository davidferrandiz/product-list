package com.davidferrandiz.mangostore.domain.usecase

import com.davidferrandiz.mangostore.core.testing.product
import com.davidferrandiz.mangostore.domain.repository.FavoriteRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ToggleFavoriteUseCaseTest {

    private val favoriteRepository: FavoriteRepository = mockk(relaxed = true)

    private val toggleFavorite = ToggleFavoriteUseCase(favoriteRepository)

    @Test
    fun `adds the product when it is not a favorite yet`() = runTest {
        val product = product(id = 7, isFavorite = false)

        toggleFavorite(product)

        coVerify(exactly = 1) { favoriteRepository.add(product) }
        coVerify(exactly = 0) { favoriteRepository.remove(any()) }
    }

    @Test
    fun `removes the product by id when it is already a favorite`() = runTest {
        val product = product(id = 7, isFavorite = true)

        toggleFavorite(product)

        coVerify(exactly = 1) { favoriteRepository.remove(7) }
        coVerify(exactly = 0) { favoriteRepository.add(any()) }
    }
}
