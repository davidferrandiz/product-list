package com.davidferrandiz.mangostore.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidferrandiz.mangostore.core.ui.R
import com.davidferrandiz.mangostore.core.ui.component.ErrorContent
import com.davidferrandiz.mangostore.core.ui.component.LoadingIndicator
import com.davidferrandiz.mangostore.core.ui.theme.MangoTheme
import com.davidferrandiz.mangostore.domain.model.UserProfile

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileContent(
        uiState = uiState,
        onRetry = viewModel::onRetry,
        modifier = modifier,
    )
}

@Composable
internal fun ProfileContent(
    uiState: ProfileUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        ProfileUiState.Loading -> LoadingIndicator(modifier)

        is ProfileUiState.Error -> ErrorContent(
            messageRes = uiState.messageRes,
            onRetry = onRetry,
            modifier = modifier,
        )

        is ProfileUiState.Content -> ProfileDetail(
            profile = uiState.profile,
            favoriteCount = uiState.favoriteCount,
            modifier = modifier,
        )
    }
}

@Composable
private fun ProfileDetail(
    profile: UserProfile,
    favoriteCount: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Avatar(name = profile.displayName)

        Text(
            text = profile.displayName.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )

        FavoriteCountCard(favoriteCount = favoriteCount)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                ProfileField(label = stringResource(R.string.profile_email), value = profile.email)
                profile.phone?.let { phone ->
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    ProfileField(label = stringResource(R.string.profile_phone), value = phone)
                }
                profile.city?.let { city ->
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    ProfileField(
                        label = stringResource(R.string.profile_city),
                        value = city.replaceFirstChar { it.uppercase() },
                    )
                }
            }
        }
    }
}

@Composable
private fun Avatar(name: String) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = name.initials(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
private fun FavoriteCountCard(favoriteCount: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Text(
                text = stringResource(R.string.profile_favorites_count, favoriteCount),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun String.initials(): String = trim()
    .split(" ")
    .filter { part -> part.isNotBlank() }
    .take(2)
    .joinToString("") { part -> part.first().uppercase() }
    .ifEmpty { "?" }

@Preview
@Composable
private fun ProfileDetailPreview() {
    MangoTheme {
        ProfileDetail(
            profile = UserProfile(
                id = 8,
                displayName = "william hopkins",
                email = "william@gmail.com",
                phone = "1-478-001-0890",
                city = "mesa",
            ),
            favoriteCount = 3,
        )
    }
}
