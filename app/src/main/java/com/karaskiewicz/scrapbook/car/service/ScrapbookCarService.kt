package com.karaskiewicz.scrapbook.car.service

import android.content.pm.ApplicationInfo
import androidx.car.app.CarAppService
import androidx.car.app.validation.HostValidator
import com.karaskiewicz.scrapbook.car.session.ScrapbookCarSession

class ScrapbookCarService : CarAppService() {

  override fun createHostValidator(): HostValidator {
    return if (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
      HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
    } else {
      HostValidator.Builder(applicationContext)
        .addAllowedHosts(androidx.car.app.R.array.hosts_allowlist_sample)
        .build()
    }
  }

  override fun onCreateSession() = ScrapbookCarSession()
}
