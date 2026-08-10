package com.davidferrandiz.mangostore.data.remote.response

import com.davidferrandiz.mangostore.data.di.NetworkModule
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class ApiContractTest {

    private val json: Json = NetworkModule.provideJson()

    @Test
    fun `parses the real product payload and ignores the fields we do not model`() {
        val product = json.decodeFromString<ProductApiResponse>(REAL_PRODUCT)

        assertEquals(1, product.id)
        assertEquals("Fjallraven - Foldsack No. 1 Backpack", product.title)
        assertEquals(109.95, product.price, 0.0)
        assertEquals("men's clothing", product.category)
        assertEquals("https://fakestoreapi.com/img/81fPKd-2AYL.jpg", product.image)
    }

    @Test
    fun `falls back to empty description and category when the API omits them`() {
        val product = json.decodeFromString<ProductApiResponse>(
            """{"id":1,"title":"Backpack","price":109.95,"image":"https://example.com/1.jpg"}"""
        )

        assertEquals("", product.description)
        assertEquals("", product.category)
    }

    @Test
    fun `coerces an explicit null into the default value`() {
        val product = json.decodeFromString<ProductApiResponse>(
            """{"id":1,"title":"Backpack","price":109.95,"image":"https://example.com/1.jpg","description":null}"""
        )

        assertEquals("", product.description)
    }

    @Test
    fun `fails when a field we consider guaranteed is missing`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<ProductApiResponse>(
                """{"id":1,"title":"Backpack","price":109.95}"""
            )
        }
    }

    @Test
    fun `parses the real user payload including the nested objects`() {
        val user = json.decodeFromString<UserApiResponse>(REAL_USER)

        assertEquals(8, user.id)
        assertEquals("william@gmail.com", user.email)
        assertEquals("hopkins", user.username)
        assertEquals("william", user.name?.firstname)
        assertEquals("hopkins", user.name?.lastname)
        assertEquals("mesa", user.address?.city)
        assertEquals("1-478-001-0890", user.phone)
    }

    @Test
    fun `keeps the optional blocks null when the API omits them`() {
        val user = json.decodeFromString<UserApiResponse>(
            """{"id":8,"email":"william@gmail.com","username":"hopkins"}"""
        )

        assertNull(user.name)
        assertNull(user.address)
        assertNull(user.phone)
    }
}

private val REAL_PRODUCT = """
    {
      "id": 1,
      "title": "Fjallraven - Foldsack No. 1 Backpack",
      "price": 109.95,
      "description": "Your perfect pack for everyday use.",
      "category": "men's clothing",
      "image": "https://fakestoreapi.com/img/81fPKd-2AYL.jpg",
      "rating": { "rate": 3.9, "count": 120 }
    }
""".trimIndent()

private val REAL_USER = """
    {
      "address": {
        "geolocation": { "lat": "-37.3159", "long": "81.1496" },
        "city": "mesa",
        "street": "adams st",
        "number": 245,
        "zipcode": "13916-1005"
      },
      "id": 8,
      "email": "william@gmail.com",
      "username": "hopkins",
      "password": "William56${'$'}hj",
      "name": { "firstname": "william", "lastname": "hopkins" },
      "phone": "1-478-001-0890",
      "__v": 0
    }
""".trimIndent()
