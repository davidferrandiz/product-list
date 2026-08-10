package com.davidferrandiz.mangostore.domain.fake

import com.davidferrandiz.mangostore.domain.model.Product
import com.davidferrandiz.mangostore.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeFavoriteRepository : FavoriteRepository {

    private val favorites = MutableStateFlow<List<Product>>(emptyList())

    var observeFavoriteIdsSubscriptions = 0
        private set

    fun setFavorites(products: List<Product>) {
        favorites.value = products
    }

    override fun observeFavorites(): Flow<List<Product>> = favorites

    override fun observeFavoriteIds(): Flow<Set<Int>> {
        observeFavoriteIdsSubscriptions++
        return favorites.map { products -> products.map { product -> product.id }.toSet() }
    }

    override fun observeFavoriteCount(): Flow<Int> = favorites.map { products -> products.size }

    override suspend fun add(product: Product) {
        favorites.value = favorites.value + product
    }

    override suspend fun remove(productId: Int) {
        favorites.value = favorites.value.filterNot { product -> product.id == productId }
    }
}
