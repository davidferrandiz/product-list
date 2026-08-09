package com.davidferrandiz.mangostore.data.remote.datasource

import com.davidferrandiz.mangostore.data.remote.api.FakeStoreApi
import com.davidferrandiz.mangostore.data.remote.response.UserApiResponse
import com.davidferrandiz.mangostore.data.remote.safeApiCall
import com.davidferrandiz.mangostore.domain.common.MangoResult
import javax.inject.Inject

private const val CURRENT_USER_ID = 8

internal class UserRemoteDataSource @Inject constructor(
    private val api: FakeStoreApi,
) {

    suspend fun getCurrentUser(): MangoResult<UserApiResponse> =
        safeApiCall { api.getUser(CURRENT_USER_ID) }
}
