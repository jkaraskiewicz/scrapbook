package com.karaskiewicz.scrapbook.car.session

import android.content.Intent
import android.content.res.Configuration
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.karaskiewicz.scrapbook.car.ui.ScrapbookCarScreen
import timber.log.Timber

class ScrapbookCarSession: Session(), DefaultLifecycleObserver {

  override fun onCreateScreen(intent: Intent): Screen {
    lifecycle.addObserver(this)
    return ScrapbookCarScreen(carContext)
  }

  override fun onDestroy(owner: LifecycleOwner) {
    super.onDestroy(owner)
    Timber.d("onDestroy")
  }

  override fun onCarConfigurationChanged(newConfiguration: Configuration) {
    super.onCarConfigurationChanged(newConfiguration)
    Timber.d("onCarConfigurationChanged")
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    Timber.d("onNewIntent")
  }
}
