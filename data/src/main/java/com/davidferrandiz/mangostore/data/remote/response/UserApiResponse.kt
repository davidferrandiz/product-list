package com.davidferrandiz.mangostore.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
internal data class UserApiResponse(
    val id: Int,
    val email: String,
    val username: String,
    val name: Name? = null,
    val phone: String? = null,
    val address: Address? = null,
) {

    @Serializable
    internal data class Name(
        val firstname: String = "",
        val lastname: String = "",
    )

    @Serializable
    internal data class Address(
        val street: String = "",
        val number: Int = 0,
        val city: String = "",
        val zipcode: String = "",
    )
}
