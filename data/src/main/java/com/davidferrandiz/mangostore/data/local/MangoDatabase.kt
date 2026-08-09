package com.davidferrandiz.mangostore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davidferrandiz.mangostore.data.local.dao.FavoriteDao
import com.davidferrandiz.mangostore.data.local.entity.FavoriteProductEntity

@Database(
    entities = [FavoriteProductEntity::class],
    version = 1,
    exportSchema = false,
)
internal abstract class MangoDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
