package com.karaskiewicz.scrapbook.car.ui

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import androidx.lifecycle.lifecycleScope
import com.karaskiewicz.scrapbook.common.data.ScrapData
import com.karaskiewicz.scrapbook.database.repository.ScrapRepository
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class ScrapbookCarScreen(carContext: CarContext) : Screen(carContext) {

  private var scraps: List<ScrapData> = emptyList()

  private val scrapRepository by inject<ScrapRepository>(ScrapRepository::class.java)

  init {
    lifecycleScope.launch {
      scrapRepository.scraps.collect {
        scraps = it
        invalidate()
      }
    }
  }

  override fun onGetTemplate(): Template {
    val itemListBuilder = ItemList.Builder()
      .setNoItemsMessage("No scraps")

    scraps.forEach {
      val row = Row.Builder()
        .setTitle(it.text)
        .build()

      itemListBuilder.addItem(row)
    }

    return ListTemplate.Builder()
      .setSingleList(itemListBuilder.build())
      .setTitle("Scrapbook")
      .build()
  }
}
