package com.davidferrandiz.mangostore.data.remote.mapper

import com.davidferrandiz.mangostore.data.remote.response.UserApiResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UserMapperTest {

    @Test
    fun `builds the display name from firstname and lastname`() {
        val user = userResponse(
            name = UserApiResponse.Name(firstname = "william", lastname = "hopkins")
        )

        assertEquals("william hopkins", user.toDomain().displayName)
    }

    @Test
    fun `falls back to the username when the API sends no name block`() {
        val user = userResponse(name = null)

        assertEquals("hopkins", user.toDomain().displayName)
    }

    @Test
    fun `falls back to the username when both name parts are blank`() {
        val user = userResponse(name = UserApiResponse.Name(firstname = "", lastname = ""))

        assertEquals("hopkins", user.toDomain().displayName)
    }

    @Test
    fun `maps the city out of the address block and tolerates its absence`() {
        assertEquals("mesa", userResponse(city = "mesa").toDomain().city)
        assertNull(userResponse(city = null).toDomain().city)
    }
}

private fun userResponse(
    name: UserApiResponse.Name? = UserApiResponse.Name("william", "hopkins"),
    city: String? = "mesa",
) = UserApiResponse(
    id = 8,
    email = "william@gmail.com",
    username = "hopkins",
    name = name,
    phone = "1-478-001-0890",
    address = city?.let { UserApiResponse.Address(city = it) },
)
