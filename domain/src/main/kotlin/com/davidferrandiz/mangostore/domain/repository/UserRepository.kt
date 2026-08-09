package com.davidferrandiz.mangostore.domain.repository

import com.davidferrandiz.mangostore.domain.common.MangoResult
import com.davidferrandiz.mangostore.domain.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile(): MangoResult<UserProfile>
}
