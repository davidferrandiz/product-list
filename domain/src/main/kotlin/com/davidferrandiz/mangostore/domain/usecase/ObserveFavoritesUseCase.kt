package com.davidferrandiz.mangostore.domain.usecase

import com.davidferrandiz.mangostore.domain.model.Product
import com.davidferrandiz.mangostore.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveFavoritesUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) {

    operator fun invoke(): Flow<List<Product>> =
        favoriteRepository.observeFavorites().map { favorites ->
            favorites.map { product -> product.copy(isFavorite = true) }
        }
}
