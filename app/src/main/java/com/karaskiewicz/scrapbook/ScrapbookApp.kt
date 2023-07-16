package com.karaskiewicz.scrapbook

import android.app.Application
import com.karaskiewicz.scrapbook.database.MockDatabaseHelper
import com.karaskiewicz.scrapbook.di.modules
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class ScrapbookApp : Application() {

  private val mockDatabaseHelper by inject<MockDatabaseHelper>()

  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidLogger()
      androidContext(this@ScrapbookApp)
      modules(modules)
    }

    Timber.plant(Timber.DebugTree())

    mockDatabaseHelper.injectFakeScraps()
  }
}
