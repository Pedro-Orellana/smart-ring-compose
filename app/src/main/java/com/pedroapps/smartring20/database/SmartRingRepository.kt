package com.pedroapps.smartring20.database

import android.app.Application
import com.pedroapps.smartring20.viewmodels.SmartRingUI
import kotlinx.coroutines.flow.Flow

class SmartRingRepository(application: Application) {

    private val database = SmartRingDataBase.getInstance(application)
    private val smartRingDao = database.smartRingDAO()


    suspend fun insertNewRing(ringName: String, ringAddress: String) {
        val newRing = SmartRingEntity(address = ringAddress, ringName = ringName)
        smartRingDao.createNewRing(newRing)
    }

    suspend fun updateRing(ring: SmartRingUI) {
        val entity = ring.toSmartRingEntity()
        smartRingDao.updateRing(entity)
    }

    fun getRegisteredRing() : Flow<SmartRingEntity> {
        return smartRingDao.getRegisteredRing()
    }


}