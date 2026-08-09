package com.davidferrandiz.mangostore.data.remote.datasource

import com.davidferrandiz.mangostore.data.remote.api.FakeStoreApi
import com.davidferrandiz.mangostore.data.remote.response.ProductApiResponse
import com.davidferrandiz.mangostore.data.remote.safeApiCall
import com.davidferrandiz.mangostore.domain.common.MangoResult
import javax.inject.Inject

internal class ProductRemoteDataSource @Inject constructor(
    private val api: FakeStoreApi,
) {

    suspend fun getProducts(): MangoResult<List<ProductApiResponse>> =
        safeApiCall { api.getProducts() }
}
