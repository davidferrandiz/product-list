package com.davidferrandiz.mangostore.data.local.mapper

import com.davidferrandiz.mangostore.data.local.entity.FavoriteProductEntity
import com.davidferrandiz.mangostore.domain.model.Product

internal fun FavoriteProductEntity.toDomain(): Product = Product(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    imageUrl = imageUrl,
    isFavorite = true,
)

internal fun Product.toEntity(addedAt: Long): FavoriteProductEntity = FavoriteProductEntity(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    imageUrl = imageUrl,
    addedAt = addedAt,
)
