package com.davidferrandiz.mangostore.data.remote.datasource

import com.davidferrandiz.mangostore.data.remote.api.FakeStoreApi
import com.davidferrandiz.mangostore.data.remote.response.ProductApiResponse
import com.davidferrandiz.mangostore.data.remote.response.UserApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.coroutines.ContinuationInterceptor

@OptIn(ExperimentalCoroutinesApi::class)
class ProductRemoteDataSourceTest {

    @Test
    fun `runs the api call on the injected dispatcher`() = runTest {
        val ioDispatcher = StandardTestDispatcher(testScheduler)
        var interceptorDuringCall: ContinuationInterceptor? = null
        val api = object : FakeStoreApi {
            override suspend fun getProducts(): List<ProductApiResponse> {
                interceptorDuringCall = currentCoroutineContext()[ContinuationInterceptor]
                return emptyList()
            }

            override suspend fun getUser(id: Int): UserApiResponse =
                error("not used in this test")
        }
        val dataSource = ProductRemoteDataSource(api, ioDispatcher)

        dataSource.getProducts()

        assertEquals(ioDispatcher, interceptorDuringCall)
    }
}
