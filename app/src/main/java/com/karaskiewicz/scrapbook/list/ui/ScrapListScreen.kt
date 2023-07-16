package com.karaskiewicz.scrapbook.list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.karaskiewicz.scrapbook.common.data.ScrapData
import com.karaskiewicz.scrapbook.list.viewmodel.ScrapListViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapListScreen(
  navController: NavHostController,
  scrapListViewModel: ScrapListViewModel = koinViewModel()
) {
  val state by scrapListViewModel.state.collectAsState() // perhaps collectAsStateWithLifecycle?

  val onItemDeleted: (ScrapData) -> Unit =
    { scrap: ScrapData -> scrapListViewModel.deleteScrap(scrap) }

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Text(text = "Scrapbook")
        },
        modifier = Modifier.statusBarsPadding(),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
          containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
      )
    },
    floatingActionButton = {
      FloatingActionButton(onClick = { navController.navigate("add") }) {
        Icon(Icons.Filled.Add, contentDescription = "New scrap")
      }
    }
  ) {
    Column(
      modifier = Modifier
        .padding(it)
        .fillMaxSize()
    ) {
      ScrapList(state.scraps, onItemDeleted)
    }
  }
}

@Composable
fun ScrapRow(
  scrap: ScrapData,
  onItemDeleted: (scrap: ScrapData) -> Unit
) {
  Card(
    elevation = CardDefaults.cardElevation(4.dp),
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(2.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp, 4.dp)) {
      Text(scrap.text, modifier = Modifier.weight(1f))
      IconButton(onClick = { onItemDeleted(scrap) }) {
        Icon(
          Icons.Rounded.Delete,
          contentDescription = "Delete scrap",
          tint = MaterialTheme.colorScheme.tertiary
        )
      }
    }
  }
}

@Composable
fun ScrapList(
  scraps: List<ScrapData>,
  onItemDeleted: (scrap: ScrapData) -> Unit
) {
  // read more on differences between lazy versions of rows and columns
  LazyColumn(
    contentPadding = PaddingValues(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    items(scraps) {
      ScrapRow(it, onItemDeleted)
    }
  }
}
