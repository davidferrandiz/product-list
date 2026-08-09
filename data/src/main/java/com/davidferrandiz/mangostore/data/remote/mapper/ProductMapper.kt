package com.davidferrandiz.mangostore.data.remote.mapper

import com.davidferrandiz.mangostore.data.remote.response.ProductApiResponse
import com.davidferrandiz.mangostore.domain.model.Product

internal fun ProductApiResponse.toDomain(): Product = Product(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    imageUrl = image,
)
