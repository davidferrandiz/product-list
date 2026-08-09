package com.davidferrandiz.mangostore.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.davidferrandiz.mangostore.core.ui.R
import kotlinx.serialization.Serializable

@Serializable
data object ProductsKey : NavKey

@Serializable
data object FavoritesKey : NavKey

@Serializable
data object ProfileKey : NavKey

internal enum class TopLevelDestination(
    val key: NavKey,
    val icon: ImageVector,
    @param:StringRes val labelRes: Int,
) {
    PRODUCTS(ProductsKey, Icons.Filled.ShoppingBag, R.string.nav_products),
    FAVORITES(FavoritesKey, Icons.Filled.Favorite, R.string.nav_favorites),
    PROFILE(ProfileKey, Icons.Filled.Person, R.string.nav_profile),
}
