package com.karaskiewicz.scrapbook.car.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.karaskiewicz.scrapbook.car.data.ScrapbookCarDetailsState
import com.karaskiewicz.scrapbook.common.data.ScrapData
import com.karaskiewicz.scrapbook.database.repository.ScrapRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ScrapbookCarDetailsViewModel(
  private val scrapRepository: ScrapRepository,
  lifecycleOwner: LifecycleOwner
) {

  private val lifecycleScope = lifecycleOwner.lifecycleScope

  private val _state: MutableStateFlow<ScrapbookCarDetailsState> =
    MutableStateFlow(ScrapbookCarDetailsState.InitialState)
  val state: StateFlow<ScrapbookCarDetailsState> = _state.asStateFlow()

  fun deleteScrap(scrapData: ScrapData) {
    lifecycleScope.launch {
      scrapRepository.delete(scrapData)
      _state.value = ScrapbookCarDetailsState.NavigateBackActionState
    }
  }
}
