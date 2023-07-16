package com.karaskiewicz.scrapbook.list.data

import com.karaskiewicz.scrapbook.common.data.ScrapData

data class ScrapListState(
  val scraps: List<ScrapData> = listOf()
)
