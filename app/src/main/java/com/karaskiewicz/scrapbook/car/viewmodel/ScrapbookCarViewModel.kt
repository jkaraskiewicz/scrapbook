package com.karaskiewicz.scrapbook.car.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.karaskiewicz.scrapbook.car.data.ScrapbookCarState
import com.karaskiewicz.scrapbook.database.repository.ScrapRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScrapbookCarViewModel(
  private val scrapRepository: ScrapRepository,
  lifecycleOwner: LifecycleOwner
) {

  private val lifecycleScope = lifecycleOwner.lifecycleScope

  private val _state: MutableStateFlow<ScrapbookCarState> = MutableStateFlow(ScrapbookCarState())
  val state: StateFlow<ScrapbookCarState> = _state.asStateFlow()

  init {
    lifecycleScope.launch {
      scrapRepository.scraps.collect {
        _state.value = _state.value.copy(scraps = it)
      }
    }
  }
}
