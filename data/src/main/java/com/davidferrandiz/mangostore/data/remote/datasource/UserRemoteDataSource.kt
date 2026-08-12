package com.davidferrandiz.mangostore.data.remote.datasource

import com.davidferrandiz.mangostore.data.di.IoDispatcher
import com.davidferrandiz.mangostore.data.remote.api.FakeStoreApi
import com.davidferrandiz.mangostore.data.remote.response.UserApiResponse
import com.davidferrandiz.mangostore.data.remote.safeApiCall
import com.davidferrandiz.mangostore.domain.common.MangoResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val CURRENT_USER_ID = 8

internal class UserRemoteDataSource @Inject constructor(
    private val api: FakeStoreApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getCurrentUser(): MangoResult<UserApiResponse> =
        withContext(ioDispatcher) {
            safeApiCall { api.getUser(CURRENT_USER_ID) }
        }
}
