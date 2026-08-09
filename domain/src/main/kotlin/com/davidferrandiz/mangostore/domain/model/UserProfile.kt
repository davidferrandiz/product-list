package com.davidferrandiz.mangostore.domain.model

data class UserProfile(
    val id: Int,
    val displayName: String,
    val email: String,
    val phone: String?,
    val city: String?,
)
