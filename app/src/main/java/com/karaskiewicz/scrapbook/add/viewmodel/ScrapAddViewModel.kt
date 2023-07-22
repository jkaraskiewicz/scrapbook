package com.karaskiewicz.scrapbook.add.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karaskiewicz.scrapbook.add.data.ScrapAddState
import com.karaskiewicz.scrapbook.common.SharedContent
import com.karaskiewicz.scrapbook.common.data.ScrapData
import com.karaskiewicz.scrapbook.database.repository.ScrapRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScrapAddViewModel(
  private val scrapRepository: ScrapRepository,
  savedStateHandle: SavedStateHandle
) : ViewModel() {

  private val _state = MutableStateFlow(ScrapAddState())
  val state: StateFlow<ScrapAddState> = _state.asStateFlow()

  fun updateTextContent(textContent: String) {
    _state.value = _state.value.copy(text = textContent)
  }

  fun saveScrap() = viewModelScope.launch {
    val scrapText = state.value.text
    if (scrapText.isNotEmpty()) {
      scrapRepository.insert(ScrapData(state.value.text))
    }
  }

  fun onContentShared(sharedContent: SharedContent) {
    if (sharedContent is SharedContent.Text) {
      _state.value = _state.value.copy(text = sharedContent.content)
    }
  }
}
