package com.karaskiewicz.scrapbook.common

import android.content.Intent

fun Intent.parseSharedContent(): SharedContent {
  if (action != Intent.ACTION_SEND) {
    return SharedContent.Empty
  }

  if (type?.startsWith("text/") == false) {
    return SharedContent.Empty
  }

  val textContent = getStringExtra(Intent.EXTRA_TEXT) ?: ""
  return SharedContent.Text(textContent)
}

sealed class SharedContent {
  class Text(val content: String) : SharedContent()
  object Empty : SharedContent()
}
