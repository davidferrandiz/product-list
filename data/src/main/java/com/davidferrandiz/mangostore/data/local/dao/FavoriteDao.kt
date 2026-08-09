package com.davidferrandiz.mangostore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.davidferrandiz.mangostore.data.local.entity.FavoriteProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface FavoriteDao {

    @Query("SELECT * FROM favorite_products ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<FavoriteProductEntity>>

    @Query("SELECT id FROM favorite_products")
    fun observeIds(): Flow<List<Int>>

    @Query("SELECT COUNT(*) FROM favorite_products")
    fun observeCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteProductEntity)

    @Query("DELETE FROM favorite_products WHERE id = :productId")
    suspend fun deleteById(productId: Int)
}
