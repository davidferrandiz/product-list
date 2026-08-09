package com.davidferrandiz.mangostore.domain.repository

import com.davidferrandiz.mangostore.domain.common.MangoResult
import com.davidferrandiz.mangostore.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): MangoResult<List<Product>>
}
