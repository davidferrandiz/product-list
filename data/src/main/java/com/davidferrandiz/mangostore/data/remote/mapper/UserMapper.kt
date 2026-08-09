package com.davidferrandiz.mangostore.data.remote.mapper

import com.davidferrandiz.mangostore.data.remote.response.UserApiResponse
import com.davidferrandiz.mangostore.domain.model.UserProfile

internal fun UserApiResponse.toDomain(): UserProfile = UserProfile(
    id = id,
    displayName = resolveDisplayName(),
    email = email,
    phone = phone,
    city = address?.city,
)

private fun UserApiResponse.resolveDisplayName(): String {
    val fullName = name?.let { "${it.firstname} ${it.lastname}".trim() }
    return if (fullName.isNullOrBlank()) username else fullName
}
