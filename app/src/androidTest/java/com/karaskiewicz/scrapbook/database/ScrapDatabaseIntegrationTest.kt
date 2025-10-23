package com.karaskiewicz.scrapbook.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.karaskiewicz.scrapbook.database.dao.ScrapDao
import com.karaskiewicz.scrapbook.database.entity.ScrapEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class ScrapDatabaseIntegrationTest {

    private lateinit var database: ScrapDatabase
    private lateinit var scrapDao: ScrapDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            ScrapDatabase::class.java
        ).build()
        scrapDao = database.scrapDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveScrap() = runTest {
        // Given
        val uuid = UUID.randomUUID()
        val scrap = ScrapEntity(0, uuid, "Test scrap")

        // When
        scrapDao.insert(scrap)

        // Then
        scrapDao.getScraps().test {
            val scraps = awaitItem()
            assertThat(scraps).hasSize(1)
            assertThat(scraps[0].text).isEqualTo("Test scrap")
            assertThat(scraps[0].uuid).isEqualTo(uuid)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteScrapByUUID() = runTest {
        // Given
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()
        val scrap1 = ScrapEntity(0, uuid1, "First scrap")
        val scrap2 = ScrapEntity(0, uuid2, "Second scrap")
        
        scrapDao.insert(scrap1)
        scrapDao.insert(scrap2)

        // When
        scrapDao.delete(uuid1)

        // Then
        scrapDao.getScraps().test {
            val scraps = awaitItem()
            assertThat(scraps).hasSize(1)
            assertThat(scraps[0].uuid).isEqualTo(uuid2)
            assertThat(scraps[0].text).isEqualTo("Second scrap")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteScrapWithSameUUIDValue() = runTest {
        // Given - Testing potential UUID comparison bug
        val uuidString = "550e8400-e29b-41d4-a716-446655440000"
        val uuid1 = UUID.fromString(uuidString)
        val scrap = ScrapEntity(0, uuid1, "Test scrap")
        
        scrapDao.insert(scrap)

        // When - Delete using a different UUID instance with same value
        val uuid2 = UUID.fromString(uuidString)
        scrapDao.delete(uuid2)

        // Then - Should be deleted successfully
        scrapDao.getScraps().test {
            val scraps = awaitItem()
            assertThat(scraps).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteNonExistentScrap() = runTest {
        // Given
        val existingUuid = UUID.randomUUID()
        val nonExistentUuid = UUID.randomUUID()
        val scrap = ScrapEntity(0, existingUuid, "Existing scrap")
        
        scrapDao.insert(scrap)

        // When
        scrapDao.delete(nonExistentUuid)

        // Then - Original scrap should still exist
        scrapDao.getScraps().test {
            val scraps = awaitItem()
            assertThat(scraps).hasSize(1)
            assertThat(scraps[0].uuid).isEqualTo(existingUuid)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun rapidDeleteOperations() = runTest {
        // Given - Testing rapid deletions like in car mode
        val scraps = (1..5).map { 
            ScrapEntity(0, UUID.randomUUID(), "Scrap $it")
        }
        
        scraps.forEach { scrapDao.insert(it) }

        // When - Rapidly delete all scraps
        scraps.forEach { scrapDao.delete(it.uuid) }

        // Then
        scrapDao.getScraps().test {
            val remaining = awaitItem()
            assertThat(remaining).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteAllScraps() = runTest {
        // Given
        val scraps = (1..10).map { 
            ScrapEntity(0, UUID.randomUUID(), "Scrap $it")
        }
        
        scraps.forEach { scrapDao.insert(it) }

        // When
        scrapDao.deleteAll()

        // Then
        scrapDao.getScraps().test {
            val remaining = awaitItem()
            assertThat(remaining).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
        
        scrapDao.getScrapsCount().test {
            val count = awaitItem()
            assertThat(count).isEqualTo(0)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun scrapsOrderedByIdAscending() = runTest {
        // Given
        val scrap3 = ScrapEntity(0, UUID.randomUUID(), "Third")
        val scrap1 = ScrapEntity(0, UUID.randomUUID(), "First")
        val scrap2 = ScrapEntity(0, UUID.randomUUID(), "Second")
        
        // Insert in random order
        scrapDao.insert(scrap3)
        scrapDao.insert(scrap1)
        scrapDao.insert(scrap2)

        // Then - Should be ordered by ID (insertion order)
        scrapDao.getScraps().test {
            val scraps = awaitItem()
            assertThat(scraps).hasSize(3)
            assertThat(scraps[0].text).isEqualTo("Third")
            assertThat(scraps[1].text).isEqualTo("First")
            assertThat(scraps[2].text).isEqualTo("Second")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun duplicateUUIDIgnored() = runTest {
        // Given
        val uuid = UUID.randomUUID()
        val scrap1 = ScrapEntity(0, uuid, "First scrap")
        val scrap2 = ScrapEntity(0, uuid, "Second scrap with same UUID")
        
        // When
        scrapDao.insert(scrap1)
        scrapDao.insert(scrap2) // Should be ignored due to OnConflictStrategy.IGNORE

        // Then
        scrapDao.getScraps().test {
            val scraps = awaitItem()
            assertThat(scraps).hasSize(1)
            assertThat(scraps[0].text).isEqualTo("First scrap")
            cancelAndIgnoreRemainingEvents()
        }
    }
}
