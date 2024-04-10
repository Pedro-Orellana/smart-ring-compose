package com.pedroapps.smartring20.ble

import com.mbientlab.metawear.data.EulerAngles
import com.mbientlab.metawear.module.Led.Color

data class SmartRingState(
    var isConnected: Boolean = false,
    var currentLedColor: Color? = null,
    var doubleTapCount: Int = 0,
    var rawAngleData: EulerAngles? = null
)
