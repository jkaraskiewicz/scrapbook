package com.karaskiewicz.scrapbook

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.core.util.Consumer
import androidx.navigation.compose.rememberNavController

class ScrapbookActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val navController = rememberNavController()

      DisposableEffect(Unit) {
        val listener = Consumer<Intent> {
          navController.handleDeepLink(it)
        }
        addOnNewIntentListener(listener)
        onDispose { removeOnNewIntentListener(listener) }
      }

      ScrapbookScreen(navController)
    }
  }
}
