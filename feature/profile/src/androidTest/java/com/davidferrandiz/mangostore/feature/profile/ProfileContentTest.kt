package com.davidferrandiz.mangostore.feature.profile

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.davidferrandiz.mangostore.core.ui.R
import com.davidferrandiz.mangostore.core.ui.component.LOADING_INDICATOR_TAG
import com.davidferrandiz.mangostore.core.ui.theme.MangoTheme
import com.davidferrandiz.mangostore.domain.model.UserProfile
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun showsTheLoadingIndicatorWhileLoading() {
        setContent(ProfileUiState.Loading)

        composeRule.onNodeWithTag(LOADING_INDICATOR_TAG).assertIsDisplayed()
    }

    @Test
    fun showsTheErrorMessageAndLetsTheUserRetry() {
        var retried = false
        setContent(
            uiState = ProfileUiState.Error(R.string.error_no_connection),
            onRetry = { retried = true },
        )

        composeRule.onNodeWithText(context.getString(R.string.error_no_connection))
            .assertIsDisplayed()

        composeRule.onNodeWithText(context.getString(R.string.action_retry)).performClick()

        assertTrue(retried)
    }

    @Test
    fun showsTheProfileWithItsInitialsAndTheFavoriteCount() {
        setContent(ProfileUiState.Content(william, favoriteCount = 3))

        composeRule.onNodeWithText("WH").assertIsDisplayed()
        composeRule.onNodeWithText("William hopkins").assertIsDisplayed()
        composeRule.onNodeWithText("william@gmail.com").assertIsDisplayed()
        composeRule.onNodeWithText(
            context.resources.getQuantityString(R.plurals.profile_favorites_count, 3, 3)
        ).assertIsDisplayed()
    }

    @Test
    fun showsThePhoneAndTheCityWhenTheApiSendsThem() {
        setContent(ProfileUiState.Content(william, favoriteCount = 0))

        composeRule.onNodeWithText(context.getString(R.string.profile_phone)).assertIsDisplayed()
        composeRule.onNodeWithText("1-478-001-0890").assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.profile_city)).assertIsDisplayed()
        composeRule.onNodeWithText("Mesa").assertIsDisplayed()
    }

    @Test
    fun hidesThePhoneAndTheCityWhenTheApiOmitsThem() {
        setContent(
            ProfileUiState.Content(
                profile = william.copy(phone = null, city = null),
                favoriteCount = 0,
            )
        )

        composeRule.onNodeWithText("William hopkins").assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.profile_phone)).assertDoesNotExist()
        composeRule.onNodeWithText(context.getString(R.string.profile_city)).assertDoesNotExist()
    }

    @Test
    fun fallsBackToASingleInitialWhenThereIsOnlyOneWord() {
        setContent(
            ProfileUiState.Content(william.copy(displayName = "hopkins"), favoriteCount = 0)
        )

        composeRule.onNodeWithText("H").assertIsDisplayed()
    }

    private fun setContent(uiState: ProfileUiState, onRetry: () -> Unit = {}) {
        composeRule.setContent {
            MangoTheme {
                ProfileContent(uiState = uiState, onRetry = onRetry)
            }
        }
    }
}

private val william = UserProfile(
    id = 8,
    displayName = "william hopkins",
    email = "william@gmail.com",
    phone = "1-478-001-0890",
    city = "mesa",
)
