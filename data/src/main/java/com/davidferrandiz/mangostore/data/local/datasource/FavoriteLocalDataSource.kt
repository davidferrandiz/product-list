package com.davidferrandiz.mangostore.data.local.datasource

import com.davidferrandiz.mangostore.data.local.dao.FavoriteDao
import com.davidferrandiz.mangostore.data.local.entity.FavoriteProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class FavoriteLocalDataSource @Inject constructor(
    private val favoriteDao: FavoriteDao,
) {

    fun observeAll(): Flow<List<FavoriteProductEntity>> = favoriteDao.observeAll()

    fun observeIds(): Flow<List<Int>> = favoriteDao.observeIds()

    fun observeCount(): Flow<Int> = favoriteDao.observeCount()

    suspend fun save(favorite: FavoriteProductEntity) = favoriteDao.insert(favorite)

    suspend fun delete(productId: Int) = favoriteDao.deleteById(productId)
}
