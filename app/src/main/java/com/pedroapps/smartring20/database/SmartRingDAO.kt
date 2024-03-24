package com.pedroapps.smartring20.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SmartRingDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewRing(smartRing: SmartRingEntity)

    @Update
    suspend fun updateRing(smartRing: SmartRingEntity)

    @Query("SELECT * FROM smart_ring_table")
    fun getRegisteredRing() : Flow<SmartRingEntity?>

    @Delete
    suspend fun deleteRegisteredRing(ring: SmartRingEntity)

}