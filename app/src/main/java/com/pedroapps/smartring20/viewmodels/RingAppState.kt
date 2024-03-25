package com.pedroapps.smartring20.viewmodels

import android.bluetooth.BluetoothDevice
import com.pedroapps.smartring20.ble.SmartRingService

data class RingAppState(
    var isScanning: Boolean = false,
    var foundSmartRing: BluetoothDevice? = null,
    var registeredRing: SmartRingUI = SmartRingUI.emptySmartRingUI(), //needs to be of type SmartRing
    var registeredDevices: List<String> = listOf(), //needs to be of type List<SmartDevice>
    var smartRingService: SmartRingService? = null
)
