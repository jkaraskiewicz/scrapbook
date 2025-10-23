package com.karaskiewicz.scrapbook.list.ui

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.karaskiewicz.scrapbook.common.data.ScrapData
import com.karaskiewicz.scrapbook.list.data.ScrapListState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class ScrapListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyListShowsNoScraps() {
        // Given
        val emptyState = ScrapListState(scraps = emptyList())

        // When
        composeTestRule.setContent {
            ScrapList(scraps = emptyState.scraps, onItemDeleted = {})
        }

        // Then
        composeTestRule.onAllNodesWithContentDescription("Delete scrap")
            .assertCountEquals(0)
    }

    @Test
    fun scrapsAreDisplayed() {
        // Given
        val scraps = listOf(
            ScrapData(text = "First scrap"),
            ScrapData(text = "Second scrap"),
            ScrapData(text = "Third scrap")
        )

        // When
        composeTestRule.setContent {
            ScrapList(scraps = scraps, onItemDeleted = {})
        }

        // Then
        composeTestRule.onNodeWithText("First scrap").assertIsDisplayed()
        composeTestRule.onNodeWithText("Second scrap").assertIsDisplayed()
        composeTestRule.onNodeWithText("Third scrap").assertIsDisplayed()
        composeTestRule.onAllNodesWithContentDescription("Delete scrap")
            .assertCountEquals(3)
    }

    @Test
    fun clickingDeleteButtonCallsOnItemDeleted() {
        // Given
        val scrap = ScrapData(text = "Delete this scrap", uuid = UUID.randomUUID())
        var deletedScrap: ScrapData? = null
        val onItemDeleted: (ScrapData) -> Unit = { deletedScrap = it }

        // When
        composeTestRule.setContent {
            ScrapRow(scrap = scrap, onItemDeleted = onItemDeleted)
        }

        // Then
        composeTestRule.onNodeWithText("Delete this scrap").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Delete scrap").performClick()
        
        assertThat(deletedScrap).isEqualTo(scrap)
    }

    @Test
    fun multipleDeleteButtonsAreClickable() {
        // Given
        val scraps = listOf(
            ScrapData(text = "Scrap 1", uuid = UUID.randomUUID()),
            ScrapData(text = "Scrap 2", uuid = UUID.randomUUID()),
            ScrapData(text = "Scrap 3", uuid = UUID.randomUUID())
        )
        val deletedScraps = mutableListOf<ScrapData>()

        // When
        composeTestRule.setContent {
            ScrapList(scraps = scraps, onItemDeleted = { deletedScraps.add(it) })
        }

        // Delete all scraps
        composeTestRule.onAllNodesWithContentDescription("Delete scrap")[0].performClick()
        composeTestRule.onAllNodesWithContentDescription("Delete scrap")[0].performClick()
        composeTestRule.onAllNodesWithContentDescription("Delete scrap")[0].performClick()

        // Then
        assertThat(deletedScraps).hasSize(3)
        assertThat(deletedScraps).containsExactly(scraps[0], scraps[1], scraps[2])
    }

    @Test
    fun rapidDeleteClicksAreHandled() {
        // Given - Testing rapid deletion like might happen in car mode with touch/button
        val scrap = ScrapData(text = "Rapid delete test", uuid = UUID.randomUUID())
        var deleteCount = 0

        // When
        composeTestRule.setContent {
            ScrapRow(scrap = scrap, onItemDeleted = { deleteCount++ })
        }

        // Rapidly click delete 5 times
        repeat(5) {
            composeTestRule.onNodeWithContentDescription("Delete scrap").performClick()
        }

        // Then - All clicks should be registered
        assertThat(deleteCount).isEqualTo(5)
    }

    @Test
    fun longTextIsDisplayedCorrectly() {
        // Given
        val longText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
                "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad " +
                "minim veniam, quis nostrud exercitation ullamco laboris."
        val scrap = ScrapData(text = longText)

        // When
        composeTestRule.setContent {
            ScrapRow(scrap = scrap, onItemDeleted = {})
        }

        // Then
        composeTestRule.onNodeWithText(longText).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Delete scrap").assertIsDisplayed()
    }

    @Test
    fun deleteButtonIsAlwaysVisible() {
        // Given - Testing that delete button is visible even with various text lengths
        val scraps = listOf(
            ScrapData(text = "Short"),
            ScrapData(text = "A bit longer scrap text here"),
            ScrapData(text = "Very long text that might cause layout issues if not " +
                    "handled properly with the modifier weight and other compose layout parameters")
        )

        // When
        composeTestRule.setContent {
            ScrapList(scraps = scraps, onItemDeleted = {})
        }

        // Then - All delete buttons should be visible
        composeTestRule.onAllNodesWithContentDescription("Delete scrap")
            .assertCountEquals(3)
    }
}
