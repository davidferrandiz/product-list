package com.davidferrandiz.mangostore.data.repository

import com.davidferrandiz.mangostore.data.local.datasource.FavoriteLocalDataSource
import com.davidferrandiz.mangostore.data.local.mapper.toDomain
import com.davidferrandiz.mangostore.data.local.mapper.toEntity
import com.davidferrandiz.mangostore.domain.model.Product
import com.davidferrandiz.mangostore.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class FavoriteRepositoryImpl @Inject constructor(
    private val localDataSource: FavoriteLocalDataSource,
) : FavoriteRepository {

    override fun observeFavorites(): Flow<List<Product>> =
        localDataSource.observeAll()
            .map { entities -> entities.map { entity -> entity.toDomain() } }

    override fun observeFavoriteIds(): Flow<Set<Int>> =
        localDataSource.observeIds()
            .map { ids -> ids.toSet() }
            .distinctUntilChanged()

    override fun observeFavoriteCount(): Flow<Int> =
        localDataSource.observeCount()
            .distinctUntilChanged()

    override suspend fun add(product: Product) {
        localDataSource.save(product.toEntity(addedAt = System.currentTimeMillis()))
    }

    override suspend fun remove(productId: Int) {
        localDataSource.delete(productId)
    }
}
