package com.davidferrandiz.mangostore.domain.usecase

import com.davidferrandiz.mangostore.domain.model.Product
import com.davidferrandiz.mangostore.domain.repository.FavoriteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) {

    suspend operator fun invoke(product: Product) {
        if (product.isFavorite) {
            favoriteRepository.remove(product.id)
        } else {
            favoriteRepository.add(product)
        }
    }
}
