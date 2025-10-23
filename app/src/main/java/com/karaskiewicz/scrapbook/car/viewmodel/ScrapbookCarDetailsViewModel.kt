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
      try {
        scrapRepository.delete(scrapData)
        _state.value = ScrapbookCarDetailsState.NavigateBackActionState
      } catch (e: Exception) {
        timber.log.Timber.e(e, "Failed to delete scrap: ${scrapData.uuid}")
        // Don't navigate back if deletion failed, keep showing the screen
        // In a production app, you might want to show an error state here
      }
    }
  }
}
