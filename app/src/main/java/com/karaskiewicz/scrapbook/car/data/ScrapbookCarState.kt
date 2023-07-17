package com.karaskiewicz.scrapbook.car.data

import com.karaskiewicz.scrapbook.common.data.ScrapData

data class ScrapbookCarState(
  val scraps: List<ScrapData> = listOf()
)
