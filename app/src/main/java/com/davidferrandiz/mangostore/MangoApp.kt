package com.davidferrandiz.mangostore

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.davidferrandiz.mangostore.feature.favorites.FavoritesScreen
import com.davidferrandiz.mangostore.feature.products.ProductsScreen
import com.davidferrandiz.mangostore.feature.profile.ProfileScreen
import com.davidferrandiz.mangostore.navigation.FavoritesKey
import com.davidferrandiz.mangostore.navigation.ProductsKey
import com.davidferrandiz.mangostore.navigation.ProfileKey
import com.davidferrandiz.mangostore.navigation.TopLevelDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangoApp() {
    val backStack = rememberNavBackStack(ProductsKey)
    val currentDestination = TopLevelDestination.entries
        .firstOrNull { destination -> destination.key == backStack.lastOrNull() }
        ?: TopLevelDestination.PRODUCTS

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(currentDestination.labelRes),
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = NavigationBarDefaults.Elevation,
            ) {
                TopLevelDestination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = destination == currentDestination,
                        onClick = {
                            if (destination != currentDestination) {
                                backStack.switchTopLevel(destination.key)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = null,
                            )
                        },
                        label = { Text(stringResource(destination.labelRes)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                    )
                }
            }
        },
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<ProductsKey> { ProductsScreen() }
                entry<FavoritesKey> { FavoritesScreen() }
                entry<ProfileKey> { ProfileScreen() }
            },
            modifier = Modifier.padding(innerPadding),
        )
    }
}

private fun NavBackStack<NavKey>.switchTopLevel(key: NavKey) {
    clear()
    add(ProductsKey)
    if (key != ProductsKey) add(key)
}
