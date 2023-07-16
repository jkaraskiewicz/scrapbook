package com.karaskiewicz.scrapbook.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.karaskiewicz.scrapbook.database.dao.ScrapDao
import com.karaskiewicz.scrapbook.database.entity.ScrapEntity

@Database(entities = [ScrapEntity::class], version = 2, exportSchema = false)
abstract class ScrapDatabase : RoomDatabase() {

  abstract fun scrapDao(): ScrapDao
}
