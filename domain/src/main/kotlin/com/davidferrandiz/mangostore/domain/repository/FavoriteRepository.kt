package com.davidferrandiz.mangostore.domain.repository

import com.davidferrandiz.mangostore.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun observeFavorites(): Flow<List<Product>>

    fun observeFavoriteIds(): Flow<Set<Int>>

    fun observeFavoriteCount(): Flow<Int>

    suspend fun add(product: Product)

    suspend fun remove(productId: Int)
}
