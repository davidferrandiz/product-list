package com.davidferrandiz.mangostore.domain.usecase

import com.davidferrandiz.mangostore.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoriteCountUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) {

    operator fun invoke(): Flow<Int> = favoriteRepository.observeFavoriteCount()
}
