package com.karaskiewicz.scrapbook.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.karaskiewicz.scrapbook.database.entity.ScrapEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ScrapDao {

  @Query("SELECT * FROM scraps ORDER BY id ASC")
  fun getScraps(): Flow<List<ScrapEntity>>

  @Query("SELECT COUNT(*) FROM scraps")
  fun getScrapsCount(): Flow<Int>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(scrap: ScrapEntity)

  @Query("DELETE FROM scraps WHERE uuid = :uuid")
  suspend fun delete(uuid: UUID)

  @Query("DELETE FROM scraps")
  suspend fun deleteAll()
}
