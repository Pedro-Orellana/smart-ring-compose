package com.pedroapps.smartring20.database

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import com.pedroapps.smartring20.viewmodels.SmartRingUI
import kotlinx.coroutines.flow.Flow

class SmartRingRepository(application: Application) {

    private val database = SmartRingDataBase.getInstance(application)
    private val smartRingDao = database.smartRingDAO()


    @SuppressLint("MissingPermission")
    suspend fun insertNewRing(device: BluetoothDevice) {
        val newRing = SmartRingEntity(address = device.address, ringName = device.name)
        smartRingDao.createNewRing(newRing)
    }

    suspend fun updateRing(ring: SmartRingUI) {
        val entity = ring.toSmartRingEntity()
        smartRingDao.updateRing(entity)
    }

    fun getRegisteredRing() : Flow<SmartRingEntity?> {
        return smartRingDao.getRegisteredRing()
    }

    suspend fun deleteRegisteredRing(smartRingUI: SmartRingUI) {
        val entity = smartRingUI.toSmartRingEntity()
        smartRingDao.deleteRegisteredRing(entity)
    }


}