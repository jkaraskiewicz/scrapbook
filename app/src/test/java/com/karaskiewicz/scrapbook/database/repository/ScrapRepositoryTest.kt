package com.karaskiewicz.scrapbook.database.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.karaskiewicz.scrapbook.common.data.ScrapData
import com.karaskiewicz.scrapbook.database.dao.ScrapDao
import com.karaskiewicz.scrapbook.database.entity.ScrapEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.UUID

class ScrapRepositoryTest {

    private lateinit var scrapDao: ScrapDao
    private lateinit var repository: ScrapRepository

    @Before
    fun setup() {
        scrapDao = mockk(relaxed = true)
        repository = ScrapRepository(scrapDao)
    }

    @Test
    fun `scraps flow maps entities to data correctly`() = runTest {
        // Given
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()
        val entities = listOf(
            ScrapEntity(1, uuid1, "First scrap"),
            ScrapEntity(2, uuid2, "Second scrap")
        )
        val mockDao = mockk<ScrapDao>(relaxed = true) {
            coEvery { getScraps() } returns flowOf(entities)
        }
        val testRepository = ScrapRepository(mockDao)

        // When/Then
        testRepository.scraps.test {
            val scraps = awaitItem()
            assertThat(scraps).hasSize(2)
            assertThat(scraps[0].text).isEqualTo("First scrap")
            assertThat(scraps[0].uuid).isEqualTo(uuid1)
            assertThat(scraps[1].text).isEqualTo("Second scrap")
            assertThat(scraps[1].uuid).isEqualTo(uuid2)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `scrapsCount returns correct count from dao`() = runTest {
        // Given
        val mockDao = mockk<ScrapDao>(relaxed = true) {
            coEvery { getScraps() } returns flowOf(emptyList())
            coEvery { getScrapsCount() } returns flowOf(5)
        }
        val testRepository = ScrapRepository(mockDao)

        // When/Then
        testRepository.scrapsCount.test {
            val count = awaitItem()
            assertThat(count).isEqualTo(5)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `insert calls dao with correct entity`() = runTest {
        // Given
        val scrapData = ScrapData(text = "Test scrap")

        // When
        repository.insert(scrapData)

        // Then
        coVerify {
            scrapDao.insert(
                match {
                    it.text == "Test scrap" && it.uuid == scrapData.uuid
                }
            )
        }
    }

    @Test
    fun `delete calls dao with correct UUID`() = runTest {
        // Given
        val uuid = UUID.randomUUID()
        val scrapData = ScrapData(text = "Test scrap", uuid = uuid)

        // When
        repository.delete(scrapData)

        // Then
        coVerify { scrapDao.delete(uuid) }
    }

    @Test
    fun `delete with different UUID instances should still work`() = runTest {
        // Given - This tests the potential bug where UUID comparison might fail
        val uuidString = "550e8400-e29b-41d4-a716-446655440000"
        val uuid1 = UUID.fromString(uuidString)
        val uuid2 = UUID.fromString(uuidString) // Different instance, same value
        val scrapData = ScrapData(text = "Test scrap", uuid = uuid1)

        // When
        repository.delete(ScrapData(text = "Test scrap", uuid = uuid2))

        // Then - Should delete using uuid2, which equals uuid1
        coVerify { scrapDao.delete(uuid2) }
        assertThat(uuid1).isEqualTo(uuid2) // Verify UUIDs are equal by value
    }

    @Test
    fun `deleteAll calls dao deleteAll`() = runTest {
        // When
        repository.deleteAll()

        // Then
        coVerify { scrapDao.deleteAll() }
    }

    @Test
    fun `empty scraps list returns empty data list`() = runTest {
        // Given
        val mockDao = mockk<ScrapDao>(relaxed = true) {
            coEvery { getScraps() } returns flowOf(emptyList())
        }
        val testRepository = ScrapRepository(mockDao)

        // When/Then
        testRepository.scraps.test {
            val scraps = awaitItem()
            assertThat(scraps).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
