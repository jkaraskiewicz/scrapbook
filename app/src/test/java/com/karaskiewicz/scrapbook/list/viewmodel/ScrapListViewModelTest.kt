package com.karaskiewicz.scrapbook.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.karaskiewicz.scrapbook.common.data.ScrapData
import com.karaskiewicz.scrapbook.database.repository.ScrapRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class ScrapListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: ScrapRepository
    private lateinit var viewModel: ScrapListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty`() = runTest {
        // Given
        val scrapsFlow = MutableStateFlow<List<ScrapData>>(emptyList())
        coEvery { repository.scraps } returns scrapsFlow

        // When
        viewModel = ScrapListViewModel(repository)
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.scraps).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state updates when repository emits scraps`() = runTest {
        // Given
        val scrap1 = ScrapData(text = "First scrap")
        val scrap2 = ScrapData(text = "Second scrap")
        val scrapsFlow = MutableStateFlow(listOf(scrap1, scrap2))
        coEvery { repository.scraps } returns scrapsFlow

        // When
        viewModel = ScrapListViewModel(repository)
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.scraps).hasSize(2)
            assertThat(state.scraps[0].text).isEqualTo("First scrap")
            assertThat(state.scraps[1].text).isEqualTo("Second scrap")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteScrap calls repository delete with correct scrap`() = runTest {
        // Given
        val scrapsFlow = MutableStateFlow<List<ScrapData>>(emptyList())
        coEvery { repository.scraps } returns scrapsFlow
        viewModel = ScrapListViewModel(repository)
        advanceUntilIdle()

        val scrapToDelete = ScrapData(text = "Delete me", uuid = UUID.randomUUID())

        // When
        viewModel.deleteScrap(scrapToDelete)
        advanceUntilIdle()

        // Then
        coVerify { repository.delete(scrapToDelete) }
    }

    @Test
    fun `state reflects changes after repository updates`() = runTest {
        // Given
        val scrap1 = ScrapData(text = "Scrap 1")
        val scrap2 = ScrapData(text = "Scrap 2")
        val scrap3 = ScrapData(text = "Scrap 3")
        val scrapsFlow = MutableStateFlow(listOf(scrap1, scrap2, scrap3))
        coEvery { repository.scraps } returns scrapsFlow

        viewModel = ScrapListViewModel(repository)
        advanceUntilIdle()

        // When - Simulate repository removing a scrap
        scrapsFlow.value = listOf(scrap1, scrap3)
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.scraps).hasSize(2)
            assertThat(state.scraps).containsExactly(scrap1, scrap3)
            assertThat(state.scraps).doesNotContain(scrap2)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `multiple deletes are handled correctly`() = runTest {
        // Given
        val scrapsFlow = MutableStateFlow<List<ScrapData>>(emptyList())
        coEvery { repository.scraps } returns scrapsFlow
        viewModel = ScrapListViewModel(repository)
        advanceUntilIdle()

        val scrap1 = ScrapData(text = "Scrap 1")
        val scrap2 = ScrapData(text = "Scrap 2")
        val scrap3 = ScrapData(text = "Scrap 3")

        // When - Delete multiple scraps rapidly
        viewModel.deleteScrap(scrap1)
        viewModel.deleteScrap(scrap2)
        viewModel.deleteScrap(scrap3)
        advanceUntilIdle()

        // Then - All deletes should be called
        coVerify { repository.delete(scrap1) }
        coVerify { repository.delete(scrap2) }
        coVerify { repository.delete(scrap3) }
    }

    @Test
    fun `deleting the same scrap twice calls repository twice`() = runTest {
        // Given - This tests a potential issue where rapid deletion might be ignored
        val scrapsFlow = MutableStateFlow<List<ScrapData>>(emptyList())
        coEvery { repository.scraps } returns scrapsFlow
        viewModel = ScrapListViewModel(repository)
        advanceUntilIdle()

        val scrap = ScrapData(text = "Double delete", uuid = UUID.randomUUID())

        // When
        viewModel.deleteScrap(scrap)
        viewModel.deleteScrap(scrap)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 2) { repository.delete(scrap) }
    }
}
