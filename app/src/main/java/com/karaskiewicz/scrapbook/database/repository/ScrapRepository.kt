package com.karaskiewicz.scrapbook.database.repository

import com.karaskiewicz.scrapbook.common.data.ScrapData
import com.karaskiewicz.scrapbook.database.dao.ScrapDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class ScrapRepository(private val scrapDao: ScrapDao) {

  val scraps: Flow<List<ScrapData>> =
    scrapDao.getScraps().map { it.map { entity -> ScrapData.fromEntity(entity) } }

  val scrapsCount: Flow<Int> = scrapDao.getScrapsCount()

  suspend fun insert(scrapData: ScrapData) {
    scrapDao.insert(scrapData.toEntity())
  }

  suspend fun delete(scrapData: ScrapData) {
    Timber.d("Delete")
    Timber.d(scrapData.toString())
    scrapDao.delete(scrapData.uuid)
  }

  suspend fun deleteAll() {
    scrapDao.deleteAll()
  }
}
