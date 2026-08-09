package com.davidferrandiz.mangostore.domain.usecase

import com.davidferrandiz.mangostore.domain.common.MangoResult
import com.davidferrandiz.mangostore.domain.model.Product
import com.davidferrandiz.mangostore.domain.repository.FavoriteRepository
import com.davidferrandiz.mangostore.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val favoriteRepository: FavoriteRepository,
) {

    operator fun invoke(): Flow<MangoResult<List<Product>>> = flow {
        when (val result = productRepository.getProducts()) {
            is MangoResult.Error -> emit(result)
            is MangoResult.Success -> emitAll(
                favoriteRepository.observeFavoriteIds().map { favoriteIds ->
                    MangoResult.Success(
                        result.data.map { product ->
                            product.copy(isFavorite = product.id in favoriteIds)
                        }
                    )
                }
            )
        }
    }
}
