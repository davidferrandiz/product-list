package com.davidferrandiz.mangostore.data.repository

import com.davidferrandiz.mangostore.data.remote.datasource.ProductRemoteDataSource
import com.davidferrandiz.mangostore.data.remote.mapper.toDomain
import com.davidferrandiz.mangostore.domain.common.MangoResult
import com.davidferrandiz.mangostore.domain.common.map
import com.davidferrandiz.mangostore.domain.model.Product
import com.davidferrandiz.mangostore.domain.repository.ProductRepository
import javax.inject.Inject
import kotlin.collections.map

internal class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProductRemoteDataSource,
) : ProductRepository {

    override suspend fun getProducts(): MangoResult<List<Product>> =
        remoteDataSource.getProducts().map { responses ->
            responses.map { response -> response.toDomain() }
        }
}
