package com.pedroapps.smartring20.ble

import com.mbientlab.metawear.module.Led.Color

data class SmartRingState(
    var isConnected: Boolean = false,
    var currentLedColor: Color? = null
)
