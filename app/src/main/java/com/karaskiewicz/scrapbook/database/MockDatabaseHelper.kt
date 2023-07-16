package com.karaskiewicz.scrapbook.database

import com.karaskiewicz.scrapbook.common.data.ScrapData
import com.karaskiewicz.scrapbook.database.repository.ScrapRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MockDatabaseHelper(private val scrapRepository: ScrapRepository) {

  private val fakeScraps = listOf(
    ScrapData(text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."),
    ScrapData(text = "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."),
    ScrapData(text = "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."),
    ScrapData(text = "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis"),
    ScrapData(text = "Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?"),
  )

  fun injectFakeScraps() {
    GlobalScope.launch {
      val scrapsCount = scrapRepository.scrapsCount.first()
      if (scrapsCount == 0) {
        fakeScraps.forEach {
          scrapRepository.insert(it)
        }
      }
    }
  }
}
