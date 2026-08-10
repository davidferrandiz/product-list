package com.davidferrandiz.mangostore.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.davidferrandiz.mangostore.data.local.dao.FavoriteDao
import com.davidferrandiz.mangostore.data.local.entity.FavoriteProductEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteDaoTest {

    private lateinit var database: MangoDatabase
    private lateinit var dao: FavoriteDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MangoDatabase::class.java,
        ).build()
        dao = database.favoriteDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun storesAFavoriteAndReadsItBack() = runBlocking {
        dao.insert(favorite(id = 1))

        dao.observeAll().test {
            val stored = awaitItem()

            assertEquals(1, stored.size)
            assertEquals("Product 1", stored.first().title)
            assertEquals(9.99, stored.first().price, 0.0)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun ordersTheMostRecentlyAddedFirst() = runBlocking {
        dao.insert(favorite(id = 1, addedAt = 100L))
        dao.insert(favorite(id = 2, addedAt = 300L))
        dao.insert(favorite(id = 3, addedAt = 200L))

        dao.observeAll().test {
            assertEquals(listOf(2, 3, 1), awaitItem().map { entity -> entity.id })

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun replacesInsteadOfDuplicatingWhenTheSameProductIsStoredTwice() = runBlocking {
        dao.insert(favorite(id = 1, title = "Old title"))
        dao.insert(favorite(id = 1, title = "New title"))

        dao.observeAll().test {
            val stored = awaitItem()

            assertEquals(1, stored.size)
            assertEquals("New title", stored.first().title)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deletesOnlyTheRequestedProduct() = runBlocking {
        dao.insert(favorite(id = 1))
        dao.insert(favorite(id = 2))

        dao.deleteById(productId = 1)

        dao.observeAll().test {
            assertEquals(listOf(2), awaitItem().map { entity -> entity.id })

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun notifiesTheObserversWhenTheTableChanges() = runBlocking {
        dao.observeAll().test {
            assertEquals(emptyList<Int>(), awaitItem().map { entity -> entity.id })

            dao.insert(favorite(id = 1))
            assertEquals(listOf(1), awaitItem().map { entity -> entity.id })

            dao.insert(favorite(id = 2, addedAt = 500L))
            assertEquals(listOf(2, 1), awaitItem().map { entity -> entity.id })

            dao.deleteById(productId = 2)
            assertEquals(listOf(1), awaitItem().map { entity -> entity.id })

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun observesIdsAndCountConsistentlyWithTheStoredRows() = runBlocking {
        dao.insert(favorite(id = 4))
        dao.insert(favorite(id = 7))

        dao.observeIds().test {
            assertEquals(setOf(4, 7), awaitItem().toSet())

            cancelAndIgnoreRemainingEvents()
        }

        dao.observeCount().test {
            assertEquals(2, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}

private fun favorite(
    id: Int,
    title: String = "Product $id",
    addedAt: Long = id.toLong(),
) = FavoriteProductEntity(
    id = id,
    title = title,
    price = 9.99,
    description = "Description $id",
    category = "category",
    imageUrl = "https://example.com/$id.png",
    addedAt = addedAt,
)
