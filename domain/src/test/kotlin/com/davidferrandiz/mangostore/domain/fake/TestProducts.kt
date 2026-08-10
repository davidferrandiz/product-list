package com.davidferrandiz.mangostore.domain.fake

import com.davidferrandiz.mangostore.domain.model.Product

fun product(id: Int, isFavorite: Boolean = false) = Product(
    id = id,
    title = "Product $id",
    price = 9.99,
    description = "Description $id",
    category = "category",
    imageUrl = "https://example.com/$id.png",
    isFavorite = isFavorite,
)
