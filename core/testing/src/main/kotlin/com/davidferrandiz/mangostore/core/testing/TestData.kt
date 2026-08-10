package com.davidferrandiz.mangostore.core.testing

import com.davidferrandiz.mangostore.domain.model.Product
import com.davidferrandiz.mangostore.domain.model.UserProfile

fun product(id: Int, isFavorite: Boolean = false) = Product(
    id = id,
    title = "Product $id",
    price = 9.99,
    description = "Description $id",
    category = "category",
    imageUrl = "https://example.com/$id.png",
    isFavorite = isFavorite,
)

fun userProfile(
    id: Int = 8,
    displayName: String = "william hopkins",
    email: String = "william@gmail.com",
    phone: String? = "1-478-001-0890",
    city: String? = "mesa",
) = UserProfile(
    id = id,
    displayName = displayName,
    email = email,
    phone = phone,
    city = city,
)
