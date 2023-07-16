package com.karaskiewicz.scrapbook.common.data

import com.karaskiewicz.scrapbook.database.entity.ScrapEntity
import java.util.UUID

data class ScrapData(val text: String, val uuid: UUID = UUID.randomUUID()) {

  fun toEntity() = ScrapEntity(0, uuid, text)

  companion object {
    fun fromEntity(scrapEntity: ScrapEntity) =
      ScrapData(scrapEntity.text, scrapEntity.uuid)
  }
}
