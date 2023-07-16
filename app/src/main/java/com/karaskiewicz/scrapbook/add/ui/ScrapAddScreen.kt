package com.karaskiewicz.scrapbook.add.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.karaskiewicz.scrapbook.add.viewmodel.ScrapAddViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapAddScreen(
  navController: NavHostController,
  scrapAddViewModel: ScrapAddViewModel = koinViewModel()
) {
  val state by scrapAddViewModel.state.collectAsState()

  val onTextChanged =
    { newTextContent: String -> scrapAddViewModel.updateTextContent(newTextContent) }

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Text(text = "Scrapbook")
        },
        navigationIcon = {
          IconButton(
            onClick = {
              scrapAddViewModel.saveScrap()
              navController.popBackStack()
            }
          ) {
            Icon(Icons.Filled.ArrowBack, "Go back")
          }
        },
        modifier = Modifier.statusBarsPadding(),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
          containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
      )
    }
  ) {
    Column(
      modifier = Modifier
        .padding(it)
        .fillMaxSize()
    ) {
      ScrapAddContent(state.text, onTextChanged)
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapAddContent(content: String, onTextChanged: (String) -> Unit) {
  val focusRequester = remember { FocusRequester() }

  TextField(
    value = content,
    onValueChange = {
      onTextChanged(it)
    },
    modifier = Modifier
      .padding(8.dp)
      .fillMaxSize()
      .focusRequester(focusRequester)
  )

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }
}

