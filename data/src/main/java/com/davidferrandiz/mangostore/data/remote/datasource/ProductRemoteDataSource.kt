package com.davidferrandiz.mangostore.data.remote.datasource

import com.davidferrandiz.mangostore.data.di.IoDispatcher
import com.davidferrandiz.mangostore.data.remote.api.FakeStoreApi
import com.davidferrandiz.mangostore.data.remote.response.ProductApiResponse
import com.davidferrandiz.mangostore.data.remote.safeApiCall
import com.davidferrandiz.mangostore.domain.common.MangoResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ProductRemoteDataSource @Inject constructor(
    private val api: FakeStoreApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getProducts(): MangoResult<List<ProductApiResponse>> =
        withContext(ioDispatcher) {
            safeApiCall { api.getProducts() }
        }
}
