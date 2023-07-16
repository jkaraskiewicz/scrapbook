package com.karaskiewicz.scrapbook

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.karaskiewicz.scrapbook.add.ui.ScrapAddScreen
import com.karaskiewicz.scrapbook.list.ui.ScrapListScreen
import com.karaskiewicz.scrapbook.ui.theme.ScrapbookTheme

@Composable
fun ScrapbookScreen() {
  ScrapbookTheme {
    val navController = rememberNavController()
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = MaterialTheme.colorScheme.background
    ) {
      NavGraph(navController)
    }
  }
}

@Composable
fun NavGraph(navController: NavHostController) {
  NavHost(navController = navController, startDestination = "list") {
    composable(
      route = "list",
    ) {
      ScrapListScreen(navController)
    }
    composable(
      route = "add",
      deepLinks = listOf(
        navDeepLink {
          action = Intent.ACTION_SEND
          mimeType = "text/*"
        }
      )
    ) {
      ScrapAddScreen(navController)
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ScrapbookScreenPreview() {
  ScrapbookTheme {
    ScrapbookScreen()
  }
}
