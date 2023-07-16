package com.karaskiewicz.scrapbook.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karaskiewicz.scrapbook.common.data.ScrapData
import com.karaskiewicz.scrapbook.database.repository.ScrapRepository
import com.karaskiewicz.scrapbook.list.data.ScrapListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScrapListViewModel(
  private val scrapRepository: ScrapRepository
) : ViewModel() {

  private val _state: MutableStateFlow<ScrapListState> = MutableStateFlow(ScrapListState())
  val state: StateFlow<ScrapListState> = _state.asStateFlow()

  init {
    viewModelScope.launch {
      scrapRepository.scraps.collect {
        _state.value = _state.value.copy(scraps = it)
      }
    }
  }

  fun deleteScrap(scrapData: ScrapData) = viewModelScope.launch {
    scrapRepository.delete(scrapData)
  }
}
