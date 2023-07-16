package com.karaskiewicz.scrapbook.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "scraps")
class ScrapEntity(
  @PrimaryKey(autoGenerate = true) val id: Int,
  @ColumnInfo(name = "uuid") val uuid: UUID,
  @ColumnInfo(name = "text") val text: String
)
