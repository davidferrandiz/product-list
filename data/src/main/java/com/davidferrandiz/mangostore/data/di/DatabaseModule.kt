package com.davidferrandiz.mangostore.data.di

import android.content.Context
import androidx.room.Room
import com.davidferrandiz.mangostore.data.local.MangoDatabase
import com.davidferrandiz.mangostore.data.local.dao.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "mango_store.db"

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideMangoDatabase(@ApplicationContext context: Context): MangoDatabase =
        Room.databaseBuilder(
            context = context,
            klass = MangoDatabase::class.java,
            name = DATABASE_NAME,
        ).build()

    @Provides
    @Singleton
    fun provideFavoriteDao(database: MangoDatabase): FavoriteDao = database.favoriteDao()
}
