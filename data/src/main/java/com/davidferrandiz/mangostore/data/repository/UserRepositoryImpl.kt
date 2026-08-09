package com.davidferrandiz.mangostore.data.repository

import com.davidferrandiz.mangostore.data.remote.datasource.UserRemoteDataSource
import com.davidferrandiz.mangostore.data.remote.mapper.toDomain
import com.davidferrandiz.mangostore.domain.common.MangoResult
import com.davidferrandiz.mangostore.domain.common.map
import com.davidferrandiz.mangostore.domain.model.UserProfile
import com.davidferrandiz.mangostore.domain.repository.UserRepository
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
) : UserRepository {

    override suspend fun getUserProfile(): MangoResult<UserProfile> =
        remoteDataSource.getCurrentUser().map { response -> response.toDomain() }
}
