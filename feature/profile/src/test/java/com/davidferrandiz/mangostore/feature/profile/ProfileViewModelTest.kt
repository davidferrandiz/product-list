package com.davidferrandiz.mangostore.feature.profile

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.davidferrandiz.mangostore.core.ui.R
import com.davidferrandiz.mangostore.domain.common.MangoResult
import com.davidferrandiz.mangostore.domain.error.AppError
import com.davidferrandiz.mangostore.domain.model.UserProfile
import com.davidferrandiz.mangostore.domain.usecase.GetUserProfileUseCase
import com.davidferrandiz.mangostore.domain.usecase.ObserveFavoriteCountUseCase
import io.mockk.coEvery
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

class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getUserProfile: GetUserProfileUseCase = mockk()
    private val observeFavoriteCount: ObserveFavoriteCountUseCase = mockk()

    private val favoriteCount = MutableStateFlow(0)

    @Test
    fun `emits the profile together with the current favorite count`() = runTest {
        coEvery { getUserProfile() } returns MangoResult.Success(william)
        every { observeFavoriteCount() } returns favoriteCount
        favoriteCount.value = 2

        viewModel().uiState.test {
            assertEquals(ProfileUiState.Loading, awaitItem())
            assertEquals(ProfileUiState.Content(william, 2), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updates the favorite count without loading the profile again`() = runTest {
        coEvery { getUserProfile() } returns MangoResult.Success(william)
        every { observeFavoriteCount() } returns favoriteCount

        viewModel().uiState.test {
            assertEquals(ProfileUiState.Loading, awaitItem())
            assertEquals(ProfileUiState.Content(william, 0), awaitItem())

            favoriteCount.value = 3
            assertEquals(ProfileUiState.Content(william, 3), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { getUserProfile() }
    }

    @Test
    fun `translates the domain error into its own message`() = runTest {
        coEvery { getUserProfile() } returns MangoResult.Error(AppError.NoConnection)
        every { observeFavoriteCount() } returns favoriteCount

        viewModel().uiState.test {
            assertEquals(ProfileUiState.Loading, awaitItem())
            assertEquals(ProfileUiState.Error(R.string.error_no_connection), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry asks for the profile again and recovers from the error`() = runTest {
        coEvery { getUserProfile() } returnsMany listOf(
            MangoResult.Error(AppError.NoConnection),
            MangoResult.Success(william),
        )
        every { observeFavoriteCount() } returns favoriteCount

        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(ProfileUiState.Loading, awaitItem())
            assertEquals(ProfileUiState.Error(R.string.error_no_connection), awaitItem())

            viewModel.onRetry()

            assertEquals(ProfileUiState.Content(william, 0), awaitContent())

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 2) { getUserProfile() }
    }

    @Test
    fun `an unexpected exception in the favorites stream becomes a generic error`() = runTest {
        coEvery { getUserProfile() } returns MangoResult.Success(william)
        every { observeFavoriteCount() } returns explodingFlow()

        viewModel().uiState.test {
            assertEquals(ProfileUiState.Loading, awaitItem())
            assertEquals(ProfileUiState.Error(R.string.error_unknown), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun viewModel() = ProfileViewModel(getUserProfile, observeFavoriteCount)
}

private suspend fun ReceiveTurbine<ProfileUiState>.awaitContent(): ProfileUiState {
    while (true) {
        val item = awaitItem()
        if (item is ProfileUiState.Content) return item
    }
}

private fun explodingFlow(): Flow<Int> = flow {
    throw IllegalStateException("something nobody predicted")
}

private val william = UserProfile(
    id = 8,
    displayName = "william hopkins",
    email = "william@gmail.com",
    phone = "1-478-001-0890",
    city = "mesa",
)
