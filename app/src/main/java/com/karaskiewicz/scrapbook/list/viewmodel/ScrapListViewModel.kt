package com.karaskiewicz.scrapbook.list.viewmodel

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.karaskiewicz.scrapbook.common.SharedContent
import com.karaskiewicz.scrapbook.common.data.ScrapData
import com.karaskiewicz.scrapbook.common.parseSharedContent
import com.karaskiewicz.scrapbook.database.repository.ScrapRepository
import com.karaskiewicz.scrapbook.list.data.ScrapListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ScrapListViewModel(
  private val scrapRepository: ScrapRepository,
  savedStateHandle: SavedStateHandle
) : ViewModel() {

  private val _state: MutableStateFlow<ScrapListState> = MutableStateFlow(ScrapListState())
  val state: StateFlow<ScrapListState> = _state.asStateFlow()

  private val sharedContentState =
    savedStateHandle.getStateFlow(NavController.KEY_DEEP_LINK_INTENT, Intent())
      .map { intent -> intent.parseSharedContent() }
      .onEach { sharedContent ->
        if (sharedContent is SharedContent.Text) {
          _state.value = _state.value.copy(sharedScrapText = sharedContent.content)
        }
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SharedContent.Empty
      )

  init {
    viewModelScope.launch {
      scrapRepository.scraps.collect {
        _state.value = _state.value.copy(scraps = it)
      }
    }

    sharedContentState.launchIn(viewModelScope)
  }

  fun deleteScrap(scrapData: ScrapData) = viewModelScope.launch {
    scrapRepository.delete(scrapData)
  }
}
