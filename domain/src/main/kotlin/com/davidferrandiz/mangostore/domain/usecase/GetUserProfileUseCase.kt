package com.davidferrandiz.mangostore.domain.usecase

import com.davidferrandiz.mangostore.domain.common.MangoResult
import com.davidferrandiz.mangostore.domain.model.UserProfile
import com.davidferrandiz.mangostore.domain.repository.UserRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(): MangoResult<UserProfile> =
        userRepository.getUserProfile()
}
