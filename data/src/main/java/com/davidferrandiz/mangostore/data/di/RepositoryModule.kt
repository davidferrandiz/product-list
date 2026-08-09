package com.davidferrandiz.mangostore.data.di

import com.davidferrandiz.mangostore.data.repository.ProductRepositoryImpl
import com.davidferrandiz.mangostore.data.repository.UserRepositoryImpl
import com.davidferrandiz.mangostore.domain.repository.ProductRepository
import com.davidferrandiz.mangostore.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
