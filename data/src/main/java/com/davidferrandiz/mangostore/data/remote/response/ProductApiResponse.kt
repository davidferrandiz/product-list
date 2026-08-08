package com.davidferrandiz.mangostore.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
internal data class ProductApiResponse(
    val id: Int,
    val title: String,
    val price: Double,
    val image: String,
    val description: String = "",
    val category: String = "",
)
