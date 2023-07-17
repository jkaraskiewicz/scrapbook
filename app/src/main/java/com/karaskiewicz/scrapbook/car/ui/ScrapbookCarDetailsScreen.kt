package com.karaskiewicz.scrapbook.car.ui

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.Template
import androidx.lifecycle.lifecycleScope
import com.karaskiewicz.scrapbook.car.data.ScrapbookCarDetailsState
import com.karaskiewicz.scrapbook.car.viewmodel.ScrapbookCarDetailsViewModel
import com.karaskiewicz.scrapbook.common.data.ScrapData
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

class ScrapbookCarDetailsScreen(
  private val scrapData: ScrapData,
  carContext: CarContext
) : Screen(carContext) {

  private val scrapbookCarDetailsViewModel: ScrapbookCarDetailsViewModel by inject(
    ScrapbookCarDetailsViewModel::class.java
  ) {
    parametersOf(
      this
    )
  }

  init {
    lifecycleScope.launch {
      scrapbookCarDetailsViewModel.state.collect {
        when (it) {
          ScrapbookCarDetailsState.InitialState -> Unit
          ScrapbookCarDetailsState.NavigateBackActionState -> screenManager.pop()
        }
      }
    }
  }

  override fun onGetTemplate(): Template {
    val deleteAction = Action.Builder()
      .setTitle("Delete")
      .setOnClickListener { scrapbookCarDetailsViewModel.deleteScrap(scrapData) }
      .build()

    return MessageTemplate.Builder(scrapData.text)
      .setTitle("Scrapbook")
      .setActionStrip(
        ActionStrip.Builder()
          .addAction(Action.BACK)
          .addAction(deleteAction)
          .build()
      )
      .build()
  }
}
