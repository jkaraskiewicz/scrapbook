package com.karaskiewicz.scrapbook.car.ui

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import androidx.lifecycle.lifecycleScope
import com.karaskiewicz.scrapbook.car.viewmodel.ScrapbookCarViewModel
import com.karaskiewicz.scrapbook.common.data.ScrapData
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

class ScrapbookCarScreen(carContext: CarContext) : Screen(carContext) {

  private var scraps: List<ScrapData> = emptyList()

  private val scrapbookCarViewModel: ScrapbookCarViewModel by inject(ScrapbookCarViewModel::class.java) {
    parametersOf(
      this
    )
  }

  init {
    lifecycleScope.launch {
      scrapbookCarViewModel.state.collect {
        scraps = it.scraps
        invalidate()
      }
    }
  }

  override fun onGetTemplate(): Template {
    val itemListBuilder = ItemList.Builder()
      .setNoItemsMessage("No scraps!")

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
